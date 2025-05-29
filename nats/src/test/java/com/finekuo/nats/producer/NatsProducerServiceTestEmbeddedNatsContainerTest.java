package com.finekuo.nats.producer;

import com.finekuo.nats.BaseEmbeddedNatsContainerTest;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class NatsProducerServiceTestEmbeddedNatsContainerTest extends BaseEmbeddedNatsContainerTest {

    private static Connection connection;
    private NatsProducerService producerService;

    @BeforeAll
    static void setupConnection() throws Exception {
        connection = Nats.connect(natsUrl);
    }

    @AfterAll
    static void closeConnection() throws Exception {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void setupProducer() {
        producerService = new NatsProducerService(connection);
    }

    @Test
    void testPublishStringMessage() throws Exception {
        String subject = "test.subject.string";
        String message = "hello-nats";
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        Dispatcher dispatcher = connection.createDispatcher(msg -> {
            queue.offer(new String(msg.getData(), StandardCharsets.UTF_8));
        });
        dispatcher.subscribe(subject);

        producerService.publish(subject, message);
        String received = queue.poll(2, TimeUnit.SECONDS);
        assertEquals(message, received);
    }

    @Test
    void testPublishByteArrayMessage() throws Exception {
        String subject = "test.subject.bytes";
        byte[] data = {1, 2, 3, 4};
        ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(1);
        Dispatcher dispatcher = connection.createDispatcher(msg -> {
            queue.offer(msg.getData());
        });
        dispatcher.subscribe(subject);

        producerService.publish(subject, data);
        byte[] received = queue.poll(2, TimeUnit.SECONDS);
        assertArrayEquals(data, received);
    }

}

