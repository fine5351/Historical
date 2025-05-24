package com.example.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.example.config.SensitiveDataConfig; // For knowing default keys
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class SensitiveDataMaskingLayoutTest {

    private SensitiveDataMaskingLayout layout;
    private Logger logger;
    private ObjectMapper objectMapper;
    private SensitiveDataConfig sensitiveDataConfig; // To access default keys for assertion

    private static final String TEST_PATTERN = "%message"; // Simple pattern for easier assertion

    @BeforeEach
    void setUp() {
        LoggerContext loggerContext = new LoggerContext();
        logger = loggerContext.getLogger(SensitiveDataMaskingLayoutTest.class);
        objectMapper = new ObjectMapper();
        sensitiveDataConfig = new SensitiveDataConfig(); // Contains default keys

        layout = new SensitiveDataMaskingLayout();
        layout.setContext(loggerContext);
        layout.setPattern(TEST_PATTERN); // Use a simple pattern
        layout.start();
    }

    private ILoggingEvent createEvent(String message) {
        return new LoggingEvent(
                Logger.FQCN, // fqcn
                logger,                     // logger
                Level.INFO,                 // level
                message,                    // message
                null,                       // throwable
                null                        // arg array
        );
    }

    private ILoggingEvent createEvent(String message, Level level) {
        return new LoggingEvent(Logger.FQCN, logger, level, message, null, null);
    }
    
    @Test
    void testSensitiveKeyIsMasked() throws JsonProcessingException {
        String originalMessage = "{\"name\": \"testUser\", \"roc_id\": \"A123456789\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        Map<String, Object> resultJson = objectMapper.readValue(formattedMessage, Map.class);
        assertEquals("testUser", resultJson.get("name"));
        assertEquals("***", resultJson.get("roc_id"));
    }

    @Test
    void testMultipleSensitiveKeysAreMasked() throws JsonProcessingException {
        String originalMessage = "{\"roc_id\": \"A123\", \"account_number\": \"1234567890\", \"credit_card_number\": \"1111-2222-3333-4444\", \"other_info\": \"data\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        Map<String, Object> resultJson = objectMapper.readValue(formattedMessage, Map.class);
        assertEquals("***", resultJson.get("roc_id"));
        assertEquals("***", resultJson.get("account_number"));
        assertEquals("***", resultJson.get("credit_card_number"));
        assertEquals("data", resultJson.get("other_info"));
    }

    @Test
    void testNoSensitiveKeyNoChange() throws JsonProcessingException {
        String originalMessage = "{\"name\": \"testUser\", \"user_id\": \"U987\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        
        // With TEST_PATTERN = "%message", formattedMessage should be originalMessage
        assertEquals(originalMessage, formattedMessage);
    }

    @Test
    void testNonJsonMessageUnchanged() {
        String originalMessage = "This is a plain text message.";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        assertEquals(originalMessage, formattedMessage);
    }
    
    @Test
    void testEmptyJsonMessage() throws JsonProcessingException {
        String originalMessage = "{}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        assertEquals(originalMessage, formattedMessage);
    }

    @Test
    void testJsonWithNullSensitiveValueIsMasked() throws JsonProcessingException {
        String originalMessage = "{\"name\": \"testUser\", \"roc_id\": null}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        Map<String, Object> resultJson = objectMapper.readValue(formattedMessage, Map.class);
        assertEquals("testUser", resultJson.get("name"));
        assertEquals("***", resultJson.get("roc_id"));
    }
    
    @Test
    void testSensitiveKeyNotPresent() throws JsonProcessingException {
        String originalMessage = "{\"name\": \"testUser\", \"another_key\": \"value\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        assertEquals(originalMessage, formattedMessage);
    }

    // The following tests for nested structures and arrays will likely FAIL
    // with the current flat-map implementation of SensitiveDataMaskingLayout.
    // This is intended, as the task is to write tests that verify these capabilities.
    // If they fail, it indicates the layout needs enhancement.

    @Test
    void testSensitiveKeyInNestedObject_ShouldBeMasked() throws JsonProcessingException {
        // This test assumes the layout SHOULD recursively mask.
        // Current flat implementation will not mask "roc_id" here.
        String originalMessage = "{\"user\": {\"name\": \"test\", \"roc_id\": \"A123\"}, \"transaction_id\": \"T1\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        ObjectNode resultJson = (ObjectNode) objectMapper.readTree(formattedMessage);
        // If masking is recursive (expected by this test):
        assertEquals("test", resultJson.get("user").get("name").asText());
        assertEquals("***", resultJson.get("user").get("roc_id").asText()); // This will fail with current layout
        assertEquals("T1", resultJson.get("transaction_id").asText());
    }

    @Test
    void testSensitiveKeyInArray_ShouldBeMasked() throws JsonProcessingException {
        // This test assumes the layout SHOULD recursively mask items in arrays.
        // Current flat implementation will not inspect/modify arrays.
        String originalMessage = "{\"transactions\": [{\"id\": \"1\", \"roc_id\": \"A123\"}, {\"id\": \"2\", \"account_number\": \"B456\"}]}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        ObjectNode resultJson = (ObjectNode) objectMapper.readTree(formattedMessage);
        // If masking is recursive (expected by this test):
        assertEquals("1", resultJson.get("transactions").get(0).get("id").asText());
        assertEquals("***", resultJson.get("transactions").get(0).get("roc_id").asText()); // This will fail
        assertEquals("2", resultJson.get("transactions").get(1).get("id").asText());
        assertEquals("***", resultJson.get("transactions").get(1).get("account_number").asText()); // This will fail
    }
    
    @Test
    void testSensitiveKeyInMixedNestedStructure_ShouldBeMasked() throws JsonProcessingException {
        String originalMessage = "{\"user\": {\"details\": {\"roc_id\": \"XYZ789\"}}, \"cards\": [{\"type\": \"visa\", \"credit_card_number\": \"1111-2222-xxxx-yyyy\"}, {\"type\": \"mc\"}]}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        ObjectNode resultJson = (ObjectNode) objectMapper.readTree(formattedMessage);
        assertEquals("***", resultJson.get("user").get("details").get("roc_id").asText()); // This will fail
        assertEquals("visa", resultJson.get("cards").get(0).get("type").asText());
        assertEquals("***", resultJson.get("cards").get(0).get("credit_card_number").asText()); // This will fail
        assertEquals("mc", resultJson.get("cards").get(1).get("type").asText());
    }


    @Test
    void testLayoutUsesDefaultSensitiveDataConfigKeys() throws JsonProcessingException {
        // This test relies on "roc_id" being a default key in SensitiveDataConfig
        assertTrue(sensitiveDataConfig.getSensitiveKeys().contains("roc_id"), "Test assumption failed: roc_id not in default config");

        String originalMessage = "{\"roc_id\": \"sensitive_value\", \"other_key\": \"normal_value\"}";
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);

        Map<String, Object> resultJson = objectMapper.readValue(formattedMessage, Map.class);
        assertEquals("***", resultJson.get("roc_id"));
        assertEquals("normal_value", resultJson.get("other_key"));
    }

    @Test
    void testPatternApplicationWithMasking() throws JsonProcessingException {
        // Test that the pattern is still applied after masking
        layout.setPattern("%level - %message"); // A pattern that adds level info
        layout.start(); // Restart the layout to apply the new pattern

        String originalMessage = "{\"roc_id\": \"A123456789\"}";
        ILoggingEvent event = createEvent(originalMessage, Level.WARN);
        String formattedMessage = layout.doLayout(event);

        // Expected: "WARN - {\"roc_id\":\"***\"}"
        assertTrue(formattedMessage.startsWith("WARN - "), "Formatted message should start with level");
        
        String jsonPart = formattedMessage.substring("WARN - ".length());
        Map<String, Object> resultJson = objectMapper.readValue(jsonPart, Map.class);
        assertEquals("***", resultJson.get("roc_id"));
    }

    @Test
    void testMalformedJsonMessage() {
        String originalMessage = "{\"name\": \"testUser\", \"roc_id\": \"A123456789\""; // Malformed JSON (missing closing brace)
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        
        // Expect original message because JSON parsing will fail
        assertEquals(originalMessage, formattedMessage);
    }

    @Test
    void testJsonButNotAnObject() { // e.g. a JSON array, or just a JSON string literal
        String originalMessage = "[\"roc_id\", \"A123456789\"]"; // JSON array, not JSON object
        ILoggingEvent event = createEvent(originalMessage);
        String formattedMessage = layout.doLayout(event);
        // Current layout expects a Map (JSON object), so this will fail parsing to Map
        // and return the original message.
        assertEquals(originalMessage, formattedMessage);

        String originalMessage2 = "\"just a json string\"";
        ILoggingEvent event2 = createEvent(originalMessage2);
        String formattedMessage2 = layout.doLayout(event2);
        assertEquals(originalMessage2, formattedMessage2);
    }
}
