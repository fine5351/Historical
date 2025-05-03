package com.finekuo.normalcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for HTTP clients
 */
@Configuration
public class HttpClientConfig {

    /**
     * Creates a WebClient for the pkuo API
     */
    @Bean
    public WebClient pkuoWebClient() {
        return WebClient.builder()
                .baseUrl("https://pkuo")
                .build();
    }

}
