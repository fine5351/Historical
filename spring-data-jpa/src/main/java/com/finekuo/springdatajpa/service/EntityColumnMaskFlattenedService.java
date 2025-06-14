package com.finekuo.springdatajpa.service;

import com.finekuo.normalcore.util.Gsons;
import com.finekuo.springdatajpa.repository.EntityColumnMaskFlattenedRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EntityColumnMaskFlattenedService {

    private final EntityColumnMaskFlattenedRepository entityColumnMaskFlattenedRepository;

    public JsonObject mask(String account, String method, String apiUrl, JsonObject jsonObject) {
        // 將 apiUrl 轉換為 pattern
        String patternUrl = convertToPattern(apiUrl);
        var entityColumnMaskFlattened = entityColumnMaskFlattenedRepository.findByAccountAndMethodAndApiUri(account, method, patternUrl);
        if (entityColumnMaskFlattened == null) {
            return jsonObject; // No mask settings found, return original JSON
        }
        List<String> maskColumns = new Gson().fromJson(entityColumnMaskFlattened.getMaskSettings(), ArrayList.class);

        log.info("Mask settings for account '{}', apiUrl '{}':, maskSettings: {}", account, apiUrl, Gsons.toJson(maskColumns));
        JsonObject result = jsonObject.deepCopy();
        log.info("before mask: {}", Gsons.getFingerprint(result));
        applyMask(result, maskColumns);
        log.info("after mask: {}", Gsons.getFingerprint(result));
        return result;
    }

    /**
     * 將 /xxx/2 或 /xxx/abc-uuid-123 這種 URI 轉換為 /xxx/{id} pattern
     * 支援多層路徑與多種 id 格式
     */
    private String convertToPattern(String uri) {
        // 將 path segment 中為數字或 UUID 的部分轉為 {id}
        // 支援 /xxx/2, /xxx/abc-uuid-123, /xxx/2/yyy/3 這類路徑
        return uri.replaceAll("(?<=/)(\\d+|[0-9a-fA-F\\-]{32,36})(?=/|$)", "{id}");
    }

    /**
     * 根據 maskSettings（欄位名稱列表）遞迴移除 jsonObject 及其所有子物件中對應欄位
     */
    private void applyMask(JsonObject jsonObject, List<String> maskSettings) {
        // 先移除當前層級的欄位
        for (String field : maskSettings) {
            jsonObject.remove(field);
        }
        // 遞迴處理子物件
        jsonObject.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonObject()) {
                applyMask(entry.getValue().getAsJsonObject(), maskSettings);
            }
        });
    }

}