package com.finekuo.nats.consumer;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NatsConsumerServiceMockTest {

    @Mock
    private Connection connection;

    @Mock
    private Dispatcher dispatcher;

    @Mock
    private Message message;

    private NatsConsumerService consumerService;
    private static final String SUBJECT = "com.finekuo.nats.example.topic";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.createDispatcher(any())).thenReturn(dispatcher);
        when(connection.getStatus()).thenReturn(Connection.Status.CONNECTED);
        consumerService = new NatsConsumerService(connection);

        // 手动调用init方法（通常由Spring的@PostConstruct触发）
        java.lang.reflect.Method initMethod = NatsConsumerService.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(consumerService);
    }

    @Test
    void testConsumerSubscribesCorrectly() {
        // 验证创建Dispatcher
        verify(connection).createDispatcher(any(MessageHandler.class));

        // 验证订阅了正确的主题和队列组
        verify(dispatcher).subscribe(eq(SUBJECT), eq("finekuo-nats-app-group"));
    }

    @Test
    void testMessageHandling() throws InterruptedException {
        // 捕获消息处理器
        ArgumentCaptor<MessageHandler> messageHandlerCaptor = ArgumentCaptor.forClass(MessageHandler.class);
        verify(connection).createDispatcher(messageHandlerCaptor.capture());
        MessageHandler messageHandler = messageHandlerCaptor.getValue();

        // 准备测试消息
        String testMsg = "test-message";
        when(message.getData()).thenReturn(testMsg.getBytes(StandardCharsets.UTF_8));
        when(message.getSubject()).thenReturn(SUBJECT);

        // 模拟接收消息
        messageHandler.onMessage(message);

        // 根据NatsConsumerService的实际实现添加断言
        // 例如，如果它应该记录或处理消息，您可以验证这些行为
    }

}
