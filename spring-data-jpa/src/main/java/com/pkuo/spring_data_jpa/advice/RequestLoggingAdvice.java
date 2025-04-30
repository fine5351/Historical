package com.pkuo.spring_data_jpa.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class RequestLoggingAdvice implements RequestBodyAdvice {

    private final HttpServletRequest request;

    public RequestLoggingAdvice(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n=== Request Information ===\n");
        logMessage.append("Method: ").append(request.getMethod()).append("\n");
        logMessage.append("URI: ").append(request.getRequestURI()).append("\n");
        logMessage.append("Query Parameters: ").append(request.getQueryString()).append("\n");

        // Log headers
        logMessage.append("Headers:\n");
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> logMessage.append(headerName)
                        .append(": ")
                        .append(request.getHeader(headerName))
                        .append("\n"));

        // Log request body
        if (body != null) {
            logMessage.append("Request Body: ").append(body).append("\n");
        }

        log.info(logMessage.toString());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}