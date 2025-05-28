package com.finekuo.nats.controller;

import com.finekuo.nats.producer.NatsProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // Creates 'log'
@RestController
@RequestMapping("/api/nats") // Base path for all endpoints in this controller
@RequiredArgsConstructor // Creates constructor for final fields
public class NatsController {

    // private static final Logger logger = LoggerFactory.getLogger(NatsController.class); // REMOVED by Lombok @Slf4j

    private final NatsProducerService natsProducerService; // Already final

    // public NatsController(NatsProducerService natsProducerService) { // REMOVED by Lombok @RequiredArgsConstructor
    //    this.natsProducerService = natsProducerService;
    // }

    @PostMapping("/publish")
    public ResponseEntity<String> publishEvent(@RequestBody NatsPublishRequest request) {
        if (request == null || request.getSubject() == null || request.getSubject().isBlank()) {
            // Added log.warn as per example
            log.warn("Publish request rejected: Subject is required."); 
            return ResponseEntity.badRequest().body("Subject is required.");
        }
        if (request.getMessage() == null) {
            // Added log.warn as per example
            log.warn("Publish request rejected: Message is required for subject '{}'", 
                     request.getSubject() == null ? "null" : request.getSubject()); // Safe subject logging
            return ResponseEntity.badRequest().body("Message is required.");
        }

        try {
            // Using 'log' from Lombok @Slf4j
            log.info("Received request to publish to NATS subject: {}", request.getSubject());
            
            natsProducerService.publish(request.getSubject(), request.getMessage());
            
            return ResponseEntity.ok("Event published successfully to subject: " + request.getSubject());
        } catch (Exception e) {
            // Using 'log' from Lombok @Slf4j
            log.error("Error publishing event via API to NATS subject {}: {}", 
                      request.getSubject(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error publishing event: " + e.getMessage());
        }
    }
}
