package com.finekuo.normalcore.config;

import com.finekuo.normalcore.interceptor.InvokeLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final InvokeLoggingInterceptor invokeLoggingInterceptor;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        registry.addInterceptor(invokeLoggingInterceptor)
                .addPathPatterns("/**"); // 拦截所有请求
    }

}