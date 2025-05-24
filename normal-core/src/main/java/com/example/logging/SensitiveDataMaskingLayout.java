package com.example.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.event.KeyValuePair; // Correct import based on compiler error
import com.example.config.SensitiveDataConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Collections; // Added for Collections.emptyList()
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
            JsonNode rootNode = objectMapper.readTree(originalLogMessage);
            boolean modified = maskNode(rootNode, sensitiveKeys);

            if (modified) {
                String maskedMessage = objectMapper.writeValueAsString(rootNode);
                LoggingEventWrapper decoratedEvent = new LoggingEventWrapper(event, maskedMessage);
                return super.doLayout(decoratedEvent);
            } else {
                return originalFormattedMessage;
            }
        } catch (Exception e) {
            // context.addError("Failed to mask sensitive data in layout: " + e.getMessage(), e); // Optional
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

                if (sensitiveKeys.contains(key)) {
                    objectNode.set(key, new TextNode("***"));
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
    public String getThreadName() { return original.getThreadName(); }
    @Override
    public ch.qos.logback.classic.Level getLevel() { return original.getLevel(); }
    @Override
    public long getTimeStamp() { return original.getTimeStamp(); }
    @Override
    public String getLoggerName() { return original.getLoggerName(); }
    @Override
    public Object[] getArgumentArray() { return original.getArgumentArray(); }
    @Override
    public String getFormattedMessage() { return messageOverride; }
    @Override
    public ch.qos.logback.classic.spi.LoggerContextVO getLoggerContextVO() { return original.getLoggerContextVO(); }
    @Override
    public ch.qos.logback.classic.spi.IThrowableProxy getThrowableProxy() { return original.getThrowableProxy(); }
    @Override
    public StackTraceElement[] getCallerData() { return original.getCallerData(); }
    @Override
    public boolean hasCallerData() { return original.hasCallerData(); }
    @Override
    public org.slf4j.Marker getMarker() { return original.getMarker(); }
    @Override
    public java.util.Map<String, String> getMDCPropertyMap() { return original.getMDCPropertyMap(); }
    /**
     * @deprecated
     */
    @Override
    @Deprecated
    public java.util.Map<String, String> getMdc() { return original.getMdc(); }
    
    @Override
    public java.util.List<org.slf4j.event.KeyValuePair> getKeyValuePairs() {
        return Collections.emptyList(); // Return an empty list as per subtask, with correct signature
    }

    @Override
    public void prepareForDeferredProcessing() { original.prepareForDeferredProcessing(); }

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
