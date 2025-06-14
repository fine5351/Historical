package com.finekuo.springdatajpa.service;

import com.finekuo.normalcore.util.Gsons;
import com.finekuo.springdatajpa.repository.EntityColumnMaskStructureRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EntityColumnMaskStructureService {

    private final EntityColumnMaskStructureRepository entityColumnMaskStructureRepository;

    public JsonObject mask(String account, String method, String apiUrl, JsonObject jsonObject) {
        // 將 apiUrl 轉換為 pattern
        String patternUrl = convertToPattern(apiUrl);
        var entityColumnMask = entityColumnMaskStructureRepository.findByAccountAndMethodAndApiUri(account, method, patternUrl);
        if (entityColumnMask == null) {
            return jsonObject; // No mask settings found, return original JSON
        }
        String maskSettingsStr = entityColumnMask.getMaskSettings();
        log.info("Mask settings for account '{}', apiUrl '{}':, maskSettings: {}", account, apiUrl, maskSettingsStr);
        if (maskSettingsStr == null) {
            return jsonObject;
        }
        JsonObject maskSettings = new Gson().fromJson(maskSettingsStr, JsonObject.class);
        if (maskSettings == null) {
            return jsonObject;
        }
        JsonObject result = jsonObject.deepCopy();
        log.info("before mask: {}", Gsons.getFingerprint(result));
        applyMask(result, maskSettings);
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
     * 遞迴根據 maskSettings 移除 jsonObject 中對應欄位
     */
    private void applyMask(JsonObject jsonObject, JsonObject maskSettings) {
        for (String key : maskSettings.keySet()) {
            JsonElement maskValue = maskSettings.get(key);
            if (maskValue.isJsonObject()) {
                // 遞迴處理巢狀物件
                if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
                    applyMask(jsonObject.getAsJsonObject(key), maskValue.getAsJsonObject());
                }
            } else if (maskValue.isJsonPrimitive() && maskValue.getAsJsonPrimitive().isBoolean() && maskValue.getAsBoolean()) {
                // maskValue 為 true，移除該欄位前印出 fingerprint
                jsonObject.remove(key);
            }
        }
    }

}