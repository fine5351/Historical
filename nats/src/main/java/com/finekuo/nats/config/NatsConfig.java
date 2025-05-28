package com.finekuo.nats.config;

import io.nats.client.*;
// import org.slf4j.Logger; // REMOVED
// import org.slf4j.LoggerFactory; // REMOVED
import lombok.extern.slf4j.Slf4j; // ADDED
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Slf4j // Creates 'log'
@Configuration
public class NatsConfig {

    // private static final Logger logger = LoggerFactory.getLogger(NatsConfig.class); // REMOVED by Lombok @Slf4j

    @Value("${nats.server.url:nats://localhost:4222}")
    private String natsServerUrl;

    @Bean(destroyMethod = "close") // Ensure connection is closed on context shutdown
    public Connection natsConnection() {
        try {
            Options options = new Options.Builder()
                    .server(natsServerUrl)
                    .connectionTimeout(Duration.ofSeconds(5)) // Example: connection timeout
                    .reconnectWait(Duration.ofSeconds(1))    // Example: reconnect wait
                    .maxReconnects(-1)                       // Example: infinite reconnects
                    .errorListener(new ErrorListener() {
                        @Override
                        public void errorOccurred(Connection conn, String error) {
                            log.error("NATS Error: {}", error); // Using 'log'
                        }

                        @Override
                        public void exceptionOccurred(Connection conn, Exception exp) {
                            log.error("NATS Exception: {}", exp.getMessage(), exp); // Using 'log'
                        }
                        // Removed slowConsumerDetected to match example
                    })
                    .connectionListener((conn, type) -> {
                        log.info("NATS Connection Status Change: {} for connection {}", type, conn.getServerInfo() != null ? conn.getServerInfo().getServerId() : "N/A"); // Using 'log'
                        if (type == ConnectionListener.Events.CONNECTED) {
                            log.info("NATS Connected to: {}", conn.getConnectedUrl()); // Using 'log'
                        } else if (type == ConnectionListener.Events.CLOSED) {
                            log.info("NATS Connection closed."); // Using 'log'
                        } else if (type == ConnectionListener.Events.DISCONNECTED) {
                            log.warn("NATS Disconnected."); // Using 'log'
                        } else if (type == ConnectionListener.Events.RECONNECTED) {
                            log.info("NATS Reconnected to: {}", conn.getConnectedUrl()); // Using 'log'
                        }
                        // My previous RESUBSCRIBED and default cases will be removed to match example strictly here.
                    })
                    .build();
            
            // log.info("Attempting to connect to NATS server: {}", natsServerUrl); // Removed this extra log line
            Connection nc = Nats.connect(options);
            log.info("Successfully connected to NATS server: {}", natsServerUrl); // Using 'log', Matched example
            return nc;
        } catch (IOException | InterruptedException e) {
            log.error("Failed to connect to NATS server: {}. Error: {}", natsServerUrl, e.getMessage(), e); // Using 'log'
            // Depending on application requirements, you might rethrow, or return null 
            // and let health checks handle it, or even exit.
            // For now, rethrowing as a runtime exception to prevent application startup if NATS is critical.
            throw new RuntimeException("Failed to connect to NATS server", e); // Matched example
        }
    }
}
