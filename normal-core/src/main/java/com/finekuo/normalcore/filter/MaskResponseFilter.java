package com.finekuo.normalcore.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * 响应掩码过滤器
 * 负责包装请求和响应，以便于后续的日志记录和掩码处理
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MaskResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 包装请求和响应
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            // 继续过滤器链
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 确保响应正常返回
            responseWrapper.copyBodyToResponse();
        }
    }

}
