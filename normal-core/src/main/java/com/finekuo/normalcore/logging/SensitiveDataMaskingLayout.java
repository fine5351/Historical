package com.finekuo.normalcore.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.finekuo.normalcore.config.SensitiveDataConfig;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SensitiveDataMaskingLayout extends PatternLayout {

    private final SensitiveDataConfig sensitiveDataConfig;
    private final ObjectMapper objectMapper;

    public SensitiveDataMaskingLayout() {
        super();
        this.sensitiveDataConfig = new SensitiveDataConfig();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String originalFormattedMessage = super.doLayout(event);
        String originalLogMessage = event.getMessage();

        if (originalLogMessage == null || originalLogMessage.trim().isEmpty()) {
            return originalFormattedMessage;
        }

        List<String> sensitiveKeys = sensitiveDataConfig.getSensitiveKeys();

        try {
            String jsonPart = null;
            boolean isEscapedJson = false;

            if (event.getArgumentArray() != null && event.getArgumentArray().length == 1) {
                Object arg = event.getArgumentArray()[0];
                // 嘗試將參數序列化為 json
                try {
                    jsonPart = objectMapper.writeValueAsString(arg);
                } catch (Exception ignore) {
                }
            } else {
                // 傳統訊息格式，從 message 中找 json
                int jsonStart = originalLogMessage.indexOf('{');
                int jsonEnd = originalLogMessage.lastIndexOf('}');
                if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                    jsonPart = originalLogMessage.substring(jsonStart, jsonEnd + 1);
                }
            }

            // 檢查是否為被 escape 的 json 字串（例如 log.info("{}", jsonString) 但 jsonString 是 String 型態且內容是 json）
            if (jsonPart != null && jsonPart.length() > 0) {
                String trimmed = jsonPart.trim();
                // 判斷是否為被雙引號包住的 json 字串
                if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
                    // 嘗試 unescape
                    String unescaped = trimmed.substring(1, trimmed.length() - 1)
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\");
                    jsonPart = unescaped;
                    isEscapedJson = true;
                }
            }

            if (jsonPart == null) {
                return originalFormattedMessage;
            }

            JsonNode rootNode = objectMapper.readTree(jsonPart);
            boolean modified = maskNode(rootNode, sensitiveKeys);

            if (modified) {
                String maskedJson = objectMapper.writeValueAsString(rootNode);
                if (isEscapedJson) {
                    // 若原本是被 escape 的 json，需再 escape 回去
                    maskedJson = "\"" + maskedJson.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
                }
                if (event.getArgumentArray() != null && event.getArgumentArray().length == 1) {
                    LoggingEventWrapper decoratedEvent = new LoggingEventWrapper(event, maskedJson);
                    return super.doLayout(decoratedEvent);
                } else {
                    int jsonStart = originalLogMessage.indexOf('{');
                    int jsonEnd = originalLogMessage.lastIndexOf('}');
                    String maskedMessage = originalLogMessage.substring(0, jsonStart) + maskedJson + originalLogMessage.substring(jsonEnd + 1);
                    LoggingEventWrapper decoratedEvent = new LoggingEventWrapper(event, maskedMessage);
                    return super.doLayout(decoratedEvent);
                }
            } else {
                return originalFormattedMessage;
            }
        } catch (Exception e) {
            return originalFormattedMessage;
        }
    }

    private boolean maskNode(JsonNode node, List<String> sensitiveKeys) {
        boolean madeChanges = false;
        if (node == null || node.isNull()) {
            return false;
        }

        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();

                StringBuilder masked = new StringBuilder();
                // 原字串的長度是多少, masked 就要多少
                for (int i = 0; i < key.length(); i++) {
                    masked.append('*');
                }

                if (sensitiveKeys.contains(key)) {
                    objectNode.set(key, new TextNode(masked.toString()));
                    madeChanges = true;
                } else if (value.isContainerNode()) {
                    if (maskNode(value, sensitiveKeys)) {
                        madeChanges = true;
                    }
                }
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                if (maskNode(arrayNode.get(i), sensitiveKeys)) {
                    madeChanges = true;
                }
            }
        }
        return madeChanges;
    }

}

class LoggingEventWrapper implements ILoggingEvent {

    private final ILoggingEvent original;
    private final String messageOverride;

    public LoggingEventWrapper(ILoggingEvent original, String messageOverride) {
        this.original = original;
        this.messageOverride = messageOverride;
    }

    @Override
    public String getMessage() {
        return messageOverride;
    }

    @Override
    public String getThreadName() {
        return original.getThreadName();
    }

    @Override
    public ch.qos.logback.classic.Level getLevel() {
        return original.getLevel();
    }

    @Override
    public long getTimeStamp() {
        return original.getTimeStamp();
    }

    @Override
    public String getLoggerName() {
        return original.getLoggerName();
    }

    @Override
    public Object[] getArgumentArray() {
        return original.getArgumentArray();
    }

    @Override
    public String getFormattedMessage() {
        return messageOverride;
    }

    @Override
    public ch.qos.logback.classic.spi.LoggerContextVO getLoggerContextVO() {
        return original.getLoggerContextVO();
    }

    @Override
    public ch.qos.logback.classic.spi.IThrowableProxy getThrowableProxy() {
        return original.getThrowableProxy();
    }

    @Override
    public StackTraceElement[] getCallerData() {
        return original.getCallerData();
    }

    @Override
    public boolean hasCallerData() {
        return original.hasCallerData();
    }

    @Override
    public org.slf4j.Marker getMarker() {
        return original.getMarker();
    }

    @Override
    public java.util.Map<String, String> getMDCPropertyMap() {
        return original.getMDCPropertyMap();
    }

    /**
     * @deprecated
     */
    @Override
    @Deprecated
    public java.util.Map<String, String> getMdc() {
        return original.getMdc();
    }

    @Override
    public java.util.List<org.slf4j.event.KeyValuePair> getKeyValuePairs() {
        return Collections.emptyList(); // Return an empty list as per subtask, with correct signature
    }

    @Override
    public void prepareForDeferredProcessing() {
        original.prepareForDeferredProcessing();
    }

    @Override
    public long getSequenceNumber() {
        return original.getSequenceNumber();
    }

    @Override
    public int getNanoseconds() {
        return original.getNanoseconds();
    }

    @Override
    public java.util.List<org.slf4j.Marker> getMarkerList() {
        return original.getMarkerList();
    }

}
