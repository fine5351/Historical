package com.finekuo.kafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper; // Using ObjectMapper for JSON
import com.finekuo.kafka.dto.request.PublishEventRequest;
// Assuming BaseResponse is available. If not, this test might have compilation issues.
// import com.finekuo.normalcore.base.BaseResponse; 
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Kept from original, assuming it's needed for project setup
@EmbeddedKafka(
    partitions = 1,
    topics = { EventControllerTest.TEST_TOPIC },
    brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" } // Using a different port
)
// Ensures Kafka clients use the embedded broker
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.group-id=event-controller-test-group" // Define a group id for the test consumer
})
public class EventControllerTest {

    static final String TEST_TOPIC = "test-event-topic";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization

    private KafkaConsumer<String, String> consumer;

    @BeforeEach
    public void setUpKafkaConsumer() {
        Properties props = new Properties();
        // spring.embedded.kafka.brokers is a system property set by @EmbeddedKafka
        props.put("bootstrap.servers", System.getProperty("spring.embedded.kafka.brokers"));
        props.put("group.id", "tc-" + UUID.randomUUID().toString()); // Unique group.id for each test run
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest"); // Ensure consumer reads from the beginning of the topic

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TEST_TOPIC));
    }

    @AfterEach
    public void tearDownKafkaConsumer() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS) // Add a timeout to prevent test hanging indefinitely
    public void publishEvent_shouldSucceedAndBeConsumedByKafka() throws Exception {
        PublishEventRequest request = new PublishEventRequest();
        request.setTopic(TEST_TOPIC);
        request.setKey("testKey-" + UUID.randomUUID().toString());
        request.setValue("{\"message\":\"Test Event Message for " + TEST_TOPIC + "\"}");

        mockMvc.perform(post("/v1/event/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true)); // Assuming BaseResponse structure

        // Consume records from Kafka
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(15));
        
        assertThat(records.count()).isGreaterThanOrEqualTo(1);

        boolean messageFound = false;
        for (ConsumerRecord<String, String> record : records) {
            if (record.topic().equals(TEST_TOPIC) && record.key().equals(request.getKey())) {
                // Assuming the value is a JSON string as set in the request
                assertThat(record.value()).isEqualTo(request.getValue());
                messageFound = true;
                break;
            }
        }
        assertThat(messageFound).withFailMessage("Message with key %s not found in topic %s", request.getKey(), TEST_TOPIC).isTrue();
    }
}
