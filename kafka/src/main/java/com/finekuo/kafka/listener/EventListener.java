package com.finekuo.kafka.listener;

import com.finekuo.kafka.dto.request.PublishEventRequest;
import com.finekuo.normalcore.util.Jsons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventListener {

    @KafkaListener(topics = "demo-topic")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        String message = consumerRecord.value();
        if (message != null && message.trim().startsWith("{")) {
            try {
                PublishEventRequest publishEventRequest = Jsons.fromJson(message, PublishEventRequest.class);
                log.info(Jsons.toJson(publishEventRequest));
            } catch (Exception e) {
                log.warn("Failed to parse JSON to PublishEventRequest: {}", message, e);
            }
        } else {
            log.info("Received non-JSON message: {}", message);
        }
    }

}
