package com.finekuo.kafka.interceptor;

import com.finekuo.normalcore.util.Jsons;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerLoggingInterceptor implements ConsumerInterceptor<String, String> {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerLoggingInterceptor.class);

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            // 只序列化 value，或可自行組裝簡單結構
            log.info("Consume: topic={}, partition={}, offset={}, key={}, value={}",
                    record.topic(), record.partition(), record.offset(), record.key(), record.value());
        }
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        // 只記錄 topic-partition 與 offset，避免序列化 OffsetAndMetadata
        Map<String, Long> simpleOffsets = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
            String key = entry.getKey().topic() + "-" + entry.getKey().partition();
            simpleOffsets.put(key, entry.getValue().offset());
        }
        log.info("Commit: {}", Jsons.toJson(simpleOffsets));
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

}
