package com.finekuo.normalcore.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@Component
@Order(2)
public class InvokeLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== [InvokeLoggingInterceptor] Request Info ===\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("URL: ").append(request.getRequestURL()).append("\n");
        sb.append("Query: ").append(request.getQueryString()).append("\n");
        sb.append("Headers:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String h = headerNames.nextElement();
            sb.append("  ").append(h).append(": ").append(request.getHeader(h)).append("\n");
        }
        // 不在這裡印 request body，因為此時還沒被讀取
        log.debug(sb.toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== [InvokeLoggingInterceptor] Request/Response Body ===\n");
        // request body
        String requestBody = "";
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                requestBody = new String(buf, StandardCharsets.UTF_8);
            }
        } else {
            requestBody = "[not a ContentCachingRequestWrapper, body unavailable]";
        }
        sb.append("Request Body: ").append(requestBody).append("\n");

        // response body
        String responseBody = "";
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                responseBody = new String(buf, StandardCharsets.UTF_8);
            }
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException e) {
                // ignore
            }
        } else {
            responseBody = "[unavailable]";
        }
        sb.append("Response Body: ").append(responseBody).append("\n");
        log.debug(sb.toString());
    }

}
