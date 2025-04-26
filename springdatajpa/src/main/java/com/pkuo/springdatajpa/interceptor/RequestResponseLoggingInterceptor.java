package com.jkos.hackathon.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

@Slf4j
@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Wrap request to allow multiple reads
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        // Log request information
        logRequest(requestWrapper);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // This method is called after the handler is executed but before the view is rendered
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Wrap response to allow multiple reads
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Log response information
        logResponse(responseWrapper);

        // Ensure response is written back to the client
        try {
            responseWrapper.copyBodyToResponse();
        } catch (IOException e) {
            log.error("Error copying response body", e);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
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
        String requestBody = getRequestBody(request);
        if (!requestBody.isEmpty()) {
            logMessage.append("Request Body: ").append(requestBody).append("\n");
        }

        log.info(logMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n=== Response Information ===\n");
        logMessage.append("Status: ").append(response.getStatus()).append("\n");

        // Log headers
        logMessage.append("Headers:\n");
        response.getHeaderNames()
                .forEach(headerName -> logMessage.append(headerName)
                        .append(": ")
                        .append(response.getHeader(headerName))
                        .append("\n"));

        // Log response body
        String responseBody = getResponseBody(response);
        if (!responseBody.isEmpty()) {
            logMessage.append("Response Body: ").append(responseBody).append("\n");
        }

        log.info(logMessage.toString());
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, request.getCharacterEncoding());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error reading request body", e);
        }
        return "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        try {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, response.getCharacterEncoding());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error reading response body", e);
        }
        return "";
    }

}