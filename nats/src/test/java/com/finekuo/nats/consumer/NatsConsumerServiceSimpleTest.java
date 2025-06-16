package com.finekuo.nats.consumer;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 不依赖Docker的简单模拟测试
 */
class NatsConsumerServiceSimpleTest {

    @Mock
    private Connection connection;

    @Mock
    private Dispatcher dispatcher;

    private static final String SUBJECT = "com.finekuo.nats.example.topic";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.createDispatcher(any(MessageHandler.class))).thenReturn(dispatcher);
        when(connection.getStatus()).thenReturn(Connection.Status.CONNECTED);
        NatsConsumerService consumerService = new NatsConsumerService(connection);

        // 手动调用init方法（通常由Spring的@PostConstruct触发）
        java.lang.reflect.Method initMethod = NatsConsumerService.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(consumerService);
    }

    @Test
    void testConsumerSubscribesCorrectly() {
        // 验证创建Dispatcher
        verify(connection).createDispatcher(any(MessageHandler.class));

        // 验证订阅了正确的主题和队列组，使用任意字符串匹配
        verify(dispatcher).subscribe(eq(SUBJECT), anyString());
    }

}
