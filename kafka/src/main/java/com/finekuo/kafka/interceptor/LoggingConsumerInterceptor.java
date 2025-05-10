package com.finekuo.kafka.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Map;

@Slf4j
public class LoggingConsumerInterceptor implements ConsumerInterceptor<String, String> {


    @Override
    public void onCommit(Map map) {

    }


    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        records.forEach(record -> {
            log.info("Consuming message from topic: {}, key: {}, value: {}", record.topic(), record.key(), record.value());
        });
        return records;
    }

    @Override
    public void close() {
        log.info("Closing ConsumerInterceptor");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // 可選配置
    }

}
