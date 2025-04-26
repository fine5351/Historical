package com.pkuo.springdatajpa.config;

import com.pkuo.springdatajpa.client.PkuoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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

    /**
     * Creates a PkuoApiClient bean
     */
    @Bean
    public PkuoApiClient pkuoApiClient(WebClient pkuoWebClient) {
        WebClientAdapter adapter = WebClientAdapter.create(pkuoWebClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(PkuoApiClient.class);
    }

}
