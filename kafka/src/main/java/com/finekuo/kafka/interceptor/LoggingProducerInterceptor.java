package com.finekuo.kafka.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

@Slf4j
public class LoggingProducerInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        log.info("Producing message to topic: {}, key: {}, value: {}", record.topic(), record.key(), record.value());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            log.info("Message acknowledged by topic: {}, partition: {}, offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
        } else {
            log.error("Error while producing message", exception);
        }
    }

    @Override
    public void close() {
        log.info("Closing ProducerInterceptor");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // 可選配置
    }

}
