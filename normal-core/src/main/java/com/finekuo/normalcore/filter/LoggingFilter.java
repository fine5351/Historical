package com.finekuo.normalcore.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest req = (request instanceof ContentCachingRequestWrapper)
                ? request
                : new ContentCachingRequestWrapper((HttpServletRequest) request);
        ServletResponse resp = (response instanceof ContentCachingResponseWrapper)
                ? response
                : new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(req, resp);

        // 注意：不要在這裡 logging，否則 response body 會提前被消耗
        // 只需在這裡 copyBodyToResponse，確保 response 正常返回
        if (resp instanceof ContentCachingResponseWrapper) {
            ((ContentCachingResponseWrapper) resp).copyBodyToResponse();
        }
    }

}
