package com.finekuo.springdatajpa.advice;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class ResponseLoggingAdvice implements ResponseBodyAdvice<Object> {

    @SuppressWarnings("null")
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("null")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n=== Response Information ===\n");

        // Get status code from response
        if (response instanceof ServletServerHttpResponse servletResponse) {
            HttpServletResponse httpResponse = servletResponse.getServletResponse();
            logMessage.append("Status: ").append(httpResponse.getStatus()).append("\n");
        }

        // Log headers
        logMessage.append("Headers:\n");
        response.getHeaders().forEach((headerName, values) ->
                logMessage.append(headerName)
                        .append(": ")
                        .append(String.join(", ", values))
                        .append("\n"));

        // Log response body
        if (body != null) {
            logMessage.append("Response Body: ").append(body).append("\n");
        }

        log.info(logMessage.toString());
        return body;
    }

}