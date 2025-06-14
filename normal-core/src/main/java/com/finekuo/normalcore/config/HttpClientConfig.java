package com.finekuo.normalcore.config;

import com.finekuo.normalcore.constant.HttpConstant;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.util.UUID;

/**
 * Configuration for HTTP clients
 */
@Configuration
@Slf4j
public class HttpClientConfig {

    /**
     * Creates a WebClient for the pkuo API
     */
    @Bean
    public WebClient pkuoWebClient() {
        return createWebClient("https://pkuo");
    }

    /**
     * Creates a WebClient with default configuration
     *
     * @param baseUrl base URL for the WebClient
     * @return configured WebClient
     */
    private static WebClient createWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .filter(correlationIdFilter())
                .build();
    }

    /**
     * Filter to add all MDC values as HTTP headers
     */
    private static ExchangeFilterFunction correlationIdFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            ClientRequest.Builder requestBuilder = ClientRequest.from(request);

            String requestId = MDC.get(HttpConstant.X_REQUEST_ID);
            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }
            requestBuilder.header(HttpConstant.X_REQUEST_ID, requestId);

            // 從 MDC 中添加所有值作為 HTTP 請求頭
            MDC.getCopyOfContextMap().forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    // 只從 header. 開頭的 MDC 值中獲取頭資訊
                    if (key.startsWith("header.")) {
                        String headerName = key.substring(7); // 移除 "header." 前綴
                        requestBuilder.header(headerName, value);
                    }
                }
            });

            return Mono.just(requestBuilder.build());
        });
    }

}
