package com.finekuo.kafka.publisher;

import com.finekuo.normalcore.util.Jsons;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventPublisher {

    private final KafkaProducer<String, String> kafkaProducer;

    public void publish(Object event) {
        kafkaProducer.send(new ProducerRecord<>("demo-topic", Jsons.toJson(event)));
    }

}
