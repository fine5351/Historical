package com.finekuo.nats.consumer;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j // Creates 'log'
@Service
@RequiredArgsConstructor // Creates constructor for final fields
public class NatsConsumerService {

    // private static final Logger logger = LoggerFactory.getLogger(NatsConsumerService.class); // REMOVED by Lombok @Slf4j
    private final Connection natsConnection; // Was already final

    // Define a specific subject for this consumer example
    private static final String NATS_SUBJECT = "com.finekuo.nats.example.topic";
    // Define a queue group for potential load balancing
    private static final String NATS_QUEUE_GROUP = "finekuo-nats-app-group";

    // public NatsConsumerService(Connection natsConnection) { // REMOVED by Lombok @RequiredArgsConstructor
    //    this.natsConnection = natsConnection;
    // }

    @PostConstruct
    private void init() {
        if (natsConnection == null || !natsConnection.getStatus().equals(Connection.Status.CONNECTED)) {
            log.error("NATS connection is not available. Cannot set up consumer."); // Using 'log'
            // This scenario should ideally be prevented by NatsConfig ensuring a valid connection
            // or by using ApplicationReadyEvent to delay initialization.
            // For now, just logging and returning.
            return;
        }
        try {
            // Create a dispatcher to handle messages asynchronously
            // The ::handleMessage method reference tells the dispatcher to call this.handleMessage for each incoming message
            Dispatcher dispatcher = natsConnection.createDispatcher(this::handleMessage);

            // Subscribe to the subject with the queue group
            // The queue group allows multiple instances of this service to load balance messages
            dispatcher.subscribe(NATS_SUBJECT, NATS_QUEUE_GROUP);

            log.info("Subscribed to NATS subject '{}' with queue group '{}'", NATS_SUBJECT, NATS_QUEUE_GROUP); // Using 'log'
        } catch (Exception e) {
            log.error("Error setting up NATS subscription for subject '{}': {}", NATS_SUBJECT, e.getMessage(), e); // Using 'log'
            // Depending on requirements, might need more robust error handling or retry mechanisms
        }
    }

    private void handleMessage(Message msg) {
        // This is the core message processing logic.
        if (msg == null) {
            // Matched example's earlier form (from previous subtask)
            log.warn("Received a null message object. This should not happen with a standard NATS client."); // Using 'log'
            return;
        }
        try {
            // The primary info log for received messages is now in NatsLoggingAspect.
            // This log should be for debugging the raw message if still needed, or removed if redundant.
            String receivedData = new String(msg.getData(), StandardCharsets.UTF_8); // As per example
            log.debug("Raw message received on subject '{}' from SID '{}': {}", msg.getSubject(), msg.getSID(), receivedData); // Using 'log', matched example

            // Actual business logic for processing the message would go here.
            // For example:
            // myBusinessService.processNatsData(receivedData);

        } catch (Exception e) {
            log.error("Error processing NATS message from subject '{}': {}",
                    msg.getSubject(), e.getMessage(), e); // Using 'log', matched example
            // Consider:
            // 1. Acknowledging message negatively if applicable (NATS core doesn't have nacks, JetStream does)
            // 2. For critical messages, consider using NATS JetStream for features like acknowledgements, retries, and dead-letter queues (DLQs).
            // 3. If this consumer is part of a larger transaction, roll back the transaction.
        }
    }

    // Optional: @PreDestroy for cleanup.
    // For a simple dispatcher subscription, closing the main NATS connection (handled in NatsConfig)
    // is generally sufficient to clean up associated dispatchers and subscriptions.
    // However, if explicit unsubscribing or dispatcher closing is needed:
    //
    // import jakarta.annotation.PreDestroy;
    //
    // @PreDestroy
    // public void cleanup() {
    //     if (dispatcher != null && dispatcher.isActive()) { // dispatcher would need to be a field
    //         try {
    //             // Consider dispatcher.unsubscribe(NATS_SUBJECT) if you only want to stop this specific sub
    //             // Or drain and close the dispatcher if it's dedicated and won't be reused.
    //             // dispatcher.close(); // This will stop message delivery and potentially unsubscribe.
    //             log.info("NATS dispatcher for subject '{}' closed.", NATS_SUBJECT); // Using 'log'
    //         } catch (Exception e) {
    //             log.warn("Error while closing NATS dispatcher for subject '{}': {}", NATS_SUBJECT, e.getMessage(), e); // Using 'log'
    //         }
    //     }
    // }
    // Note: The 'dispatcher' variable would need to be a field for PreDestroy to access it.
    // For this example, keeping it simple as per the subtask description.
}
