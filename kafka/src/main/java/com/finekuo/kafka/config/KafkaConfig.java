package com.finekuo.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducer<String, String> kafkaProducer(KafkaProperties properties) {
        return new KafkaProducer<>(properties.buildProducerProperties());
    }

}
