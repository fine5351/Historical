package com.finekuo.nats.producer;

import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j // Creates 'log'
@Service
@RequiredArgsConstructor // Creates constructor for final fields
public class NatsProducerService {

    // private static final Logger logger = LoggerFactory.getLogger(NatsProducerService.class); // REMOVED by Lombok @Slf4j
    private final Connection natsConnection; // Was already final

    // public NatsProducerService(Connection natsConnection) { // REMOVED by Lombok @RequiredArgsConstructor
    //    this.natsConnection = natsConnection;
    // }

    /**
     * Publishes a string message to the specified NATS subject.
     *
     * @param subject The NATS subject to publish to.
     * @param message The string message to publish.
     */
    public void publish(String subject, String message) {
        if (subject == null || subject.isBlank()) {
            log.error("NATS subject cannot be null or empty."); // Using 'log'
            // Consider throwing new IllegalArgumentException("NATS subject cannot be null or blank.");
            return;
        }
        if (message == null) {
            log.warn("Publishing null message to subject: {}", subject); // Using 'log'
        }

        try {
            // Basic logging, more detailed logging can be added via AOP
            log.debug("Attempting to publish message to NATS subject: {}", subject); // Using 'log'
            byte[] messageBytes = (message == null) ? null : message.getBytes(StandardCharsets.UTF_8);
            natsConnection.publish(subject, messageBytes);
            // log.info("Successfully published message to subject: {}", subject); // Example for AOP
        } catch (Exception e) {
            // This catch block is for unexpected errors during publish.
            // Connection issues should ideally be handled by the NatsConnectionManager's reconnect logic.
            log.error("Error publishing message to NATS subject {}: {}", subject, e.getMessage(), e); // Using 'log'
            // Optionally rethrow as a custom application exception
        }
    }

    /**
     * Publishes a byte array message to the specified NATS subject.
     *
     * @param subject The NATS subject to publish to.
     * @param data    The byte array message to publish.
     */
    public void publish(String subject, byte[] data) {
        if (subject == null || subject.isBlank()) {
            log.error("NATS subject cannot be null or empty."); // Using 'log'
            // Consider throwing new IllegalArgumentException("NATS subject cannot be null or blank.");
            return;
        }
        // Note: data being null or empty is valid for NATS client.
        // If data is null, an empty message is published.

        try {
            // Basic logging, more detailed logging can be added via AOP
            log.debug("Attempting to publish byte array message to NATS subject: {}", subject); // Using 'log'
            natsConnection.publish(subject, data);
            // log.info("Successfully published byte array message to subject: {}", subject); // Example for AOP
        } catch (Exception e) {
            log.error("Error publishing byte array message to NATS subject {}: {}", subject, e.getMessage(), e); // Using 'log'
            // Optionally rethrow as a custom application exception
        }
    }
}
