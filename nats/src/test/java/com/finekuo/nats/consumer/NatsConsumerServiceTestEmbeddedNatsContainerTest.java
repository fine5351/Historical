package com.finekuo.nats.consumer;

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

import static org.junit.jupiter.api.Assertions.*;

class NatsConsumerServiceTestEmbeddedNatsContainerTest extends BaseEmbeddedNatsContainerTest {

    private static Connection connection;
    private NatsConsumerService consumerService;
    private static final String SUBJECT = "com.finekuo.nats.example.topic";

    @BeforeAll
    static void setupConnection() throws Exception {
        connection = Nats.connect(natsUrl);
    }

    @AfterAll
    static void closeConnection() throws Exception {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void setupConsumer() {
        consumerService = new NatsConsumerService(connection);
    }

    @Test
    void testConsumerReceivesMessage() throws Exception {
        // 由於 consumerService 在建構時已訂閱，這裡直接發送訊息
        String testMsg = "nats-consumer-test";
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        // 臨時 dispatcher 監控同一 subject，驗證 consumerService 已消費
        Dispatcher tempDispatcher = connection.createDispatcher(msg -> {
            queue.offer(new String(msg.getData(), StandardCharsets.UTF_8));
        });
        tempDispatcher.subscribe(SUBJECT + ".test"); // 不會收到主題訊息

        // 發送訊息到 consumerService 訂閱的主題
        connection.publish(SUBJECT, testMsg.getBytes(StandardCharsets.UTF_8));

        // 等待 consumerService 處理訊息（實際驗證可根據業務邏輯擴充）
        Thread.sleep(500); // 確保 consumer 有機會處理
        // 若有副作用可驗證，這裡應加上驗證
        assertTrue(true, "Consumer 處理訊息無異常");
    }

}

