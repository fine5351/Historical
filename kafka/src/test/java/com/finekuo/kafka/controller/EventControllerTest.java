package com.finekuo.kafka.controller;

import com.finekuo.kafka.dto.request.PublishEventRequest;
import com.finekuo.normalcore.util.Jsons;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(topics = {"demo-topic"}, // Changed to "demo-topic" to match EventPublisher
               ports = {9093}, // Using a different port to avoid conflicts if any other Kafka runs on 9092
               brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
// kafka.topic.name property is not needed as EventPublisher hardcodes it.
// spring.kafka.bootstrap-servers is automatically set by @EmbeddedKafka
@TestPropertySource(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private KafkaConsumer<String, String> consumer;

    @BeforeEach
    public void setUpKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", System.getProperty("spring.embedded.kafka.brokers"));
        props.put("group.id", "event-controller-test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("demo-topic")); // Changed to "demo-topic"
    }

    @AfterEach
    public void tearDownKafkaConsumer() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    public void publishEvent_shouldSucceedAndBeConsumedByKafka() throws Exception {
        PublishEventRequest request = new PublishEventRequest();
        request.setMessage("Test E2E Event Message");
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        request.setNow(now);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(request)))
                .andExpect(status().isOk());

        // Consume records from Kafka
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
        assertThat(records.count()).isGreaterThanOrEqualTo(1);

        boolean messageFound = false;
        for (ConsumerRecord<String, String> record : records) {
            if (record.topic().equals("demo-topic")) { // Changed to "demo-topic"
                PublishEventRequest consumedRequest = Jsons.fromJson(record.value(), PublishEventRequest.class);
                // Compare relevant fields, OffsetDateTime might have precision differences after serialization/deserialization
                if (request.getMessage().equals(consumedRequest.getMessage()) &&
                    request.getNow().toEpochSecond() == consumedRequest.getNow().toEpochSecond()) {
                    messageFound = true;
                    break;
                }
            }
        }
        assertThat(messageFound).isTrue();
    }
}
