package com.finekuo.springdatajpa.service.impl;

import com.finekuo.normalcore.util.Gsons;
import com.finekuo.springdatajpa.entity.EntityColumnMaskFlattened;
import com.finekuo.springdatajpa.entity.EntityColumnMaskStructure;
import com.finekuo.springdatajpa.repository.EntityColumnMaskFlattenedRepository;
import com.finekuo.springdatajpa.repository.EntityColumnMaskStructureRepository;
import com.finekuo.springdatajpa.service.EntityMaskService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityMaskServiceImpl implements EntityMaskService {

    private final EntityColumnMaskStructureRepository structureRepository;
    private final EntityColumnMaskFlattenedRepository flattenedRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Gson gson = new Gson();

    @Override
    public JsonObject mask(String account, String method, String apiUrl, JsonObject jsonObject) {
        if (account == null || method == null || apiUrl == null) {
            return jsonObject;
        }

        // 将 apiUrl 转换为模式
        String patternUrl = convertToPattern(apiUrl);
        log.debug("Converting URL {} to pattern {}", apiUrl, patternUrl);

        // 先尝试使用结构化掩码
        JsonObject result = applyStructureMask(account, method, patternUrl, jsonObject);
        if (result != jsonObject) {
            return result;
        }

        // 如果结构化掩码未生效，尝试使用扁平化掩码
        return applyFlattenedMask(account, method, patternUrl, jsonObject);
    }

    private JsonObject applyStructureMask(String account, String method, String patternUrl, JsonObject jsonObject) {
        EntityColumnMaskStructure maskConfig = structureRepository.findByAccountAndMethodAndApiUri(account, method, patternUrl);
        if (maskConfig == null || maskConfig.getMaskSettings() == null) {
            return jsonObject;
        }

        String maskSettingsStr = maskConfig.getMaskSettings();
        log.info("Structure mask settings for account '{}', API '{}': {}", account, patternUrl, maskSettingsStr);

        JsonObject maskSettings = gson.fromJson(maskSettingsStr, JsonObject.class);
        if (maskSettings == null) {
            return jsonObject;
        }

        JsonObject result = jsonObject.deepCopy();
        log.debug("Before structure mask: {}", Gsons.getFingerprint(result));
        applyStructureMask(result, maskSettings);
        log.debug("After structure mask: {}", Gsons.getFingerprint(result));
        return result;
    }

    private void applyStructureMask(JsonObject jsonObject, JsonObject maskSettings) {
        for (String key : maskSettings.keySet()) {
            JsonElement maskValue = maskSettings.get(key);
            if (maskValue.isJsonObject()) {
                // 递归处理嵌套对象
                if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
                    applyStructureMask(jsonObject.getAsJsonObject(key), maskValue.getAsJsonObject());
                }
            } else if (maskValue.isJsonPrimitive() && maskValue.getAsJsonPrimitive().isBoolean() && maskValue.getAsBoolean()) {
                // 掩码值为 true，移除该字段
                jsonObject.remove(key);
            }
        }
    }

    private JsonObject applyFlattenedMask(String account, String method, String patternUrl, JsonObject jsonObject) {
        EntityColumnMaskFlattened maskConfig = flattenedRepository.findByAccountAndMethodAndApiUri(account, method, patternUrl);
        if (maskConfig == null || maskConfig.getMaskSettings() == null) {
            return jsonObject;
        }

        List<String> maskColumns = gson.fromJson(maskConfig.getMaskSettings(), ArrayList.class);
        log.info("Flattened mask settings for account '{}', API '{}': {}", account, patternUrl, Gsons.toJson(maskColumns));

        JsonObject result = jsonObject.deepCopy();
        log.debug("Before flattened mask: {}", Gsons.getFingerprint(result));
        applyFlattenedMask(result, maskColumns);
        log.debug("After flattened mask: {}", Gsons.getFingerprint(result));
        return result;
    }

    private void applyFlattenedMask(JsonObject jsonObject, List<String> maskColumns) {
        // 移除当前层级的字段
        for (String field : maskColumns) {
            jsonObject.remove(field);
        }
        // 递归处理子对象
        jsonObject.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonObject()) {
                applyFlattenedMask(entry.getValue().getAsJsonObject(), maskColumns);
            }
        });
    }

    /**
     * 将 URL 转换为模式
     * 例如 /xxx/123 => /xxx/{id}
     */
    private String convertToPattern(String uri) {
        return uri.replaceAll("(?<=/)(\\d+|[0-9a-fA-F\\-]{32,36})(?=/|$)", "{id}");
    }

}
