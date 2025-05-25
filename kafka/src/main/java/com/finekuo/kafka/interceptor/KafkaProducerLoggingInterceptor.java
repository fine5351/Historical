package com.finekuo.kafka.interceptor;

import com.finekuo.normalcore.util.Jsons;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaProducerLoggingInterceptor implements ProducerInterceptor<String, String> {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerLoggingInterceptor.class);

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        log.info("Send: {}", Jsons.toJson(record.value()));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            log.info("Ack: topic={}, partition={}, offset={}, timestamp={}",
                    metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
        }
        if (exception != null) {
            log.error("[KafkaProducer] Exception: ", exception);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

}
