package com.finekuo.kafka.controller;

import com.finekuo.kafka.dto.request.PublishEventRequest;
import com.finekuo.kafka.publisher.EventPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventPublisher eventPublisher;

    @PostMapping("/")
    public void publishEvent(@RequestBody @Valid PublishEventRequest request) {
        eventPublisher.publish(request);
    }

}
