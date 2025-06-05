package com.finekuo.logging.listener;

import com.finekuo.logging.event.MethodExecutionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
@Slf4j
public class LifecycleEventListener {


    /**
     * 監聽所有已處理的 HTTP 請求
     * 當 HTTP 請求被處理後觸發
     */
    @EventListener
    public void onRequestHandled(RequestHandledEvent event) {
        log.info("HTTP 請求已處理: {} (花費時間: {} ms)",
                event.getDescription(), event.getProcessingTimeMillis());
    }

    /**
     * 監聽 Servlet 相關的 HTTP 請求
     * 包含更多與 Servlet 相關的詳細信息
     */
    @EventListener
    public void onServletRequestHandled(ServletRequestHandledEvent event) {
        log.info("Servlet HTTP 請求已處理: {}, 客戶端 IP: {}, 方法: {}, URL: {}",
                event.getDescription(), event.getClientAddress(),
                event.getMethod(), event.getRequestUrl());
    }

    /**
     * 監聽 Web 服務器初始化完成事件
     * 當內嵌的 Web 服務器初始化完成後觸發
     */
    @EventListener
    public void onServletWebServerInitialized(ServletWebServerInitializedEvent event) {
        log.info("Web 服務器已初始化完成，端口: {}", event.getWebServer().getPort());
    }

    /**
     * 監聽應用上下文刷新事件
     * 當 Spring 容器完成初始化且所有 bean 加載完成時觸發
     */
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        log.info("應用上下文已刷新: {}", event.getApplicationContext().getId());
    }

    /**
     * 監聽應用上下文啟動事件
     * 當應用上下文啟動時觸發
     */
    @EventListener
    public void onContextStarted(ContextStartedEvent event) {
        log.info("應用上下文已啟動: {}", event.getApplicationContext().getId());
    }

    /**
     * 監聽應用上下文停止事件
     * 當應用上下文停止時觸發
     */
    @EventListener
    public void onContextStopped(ContextStoppedEvent event) {
        log.info("應用上下文已停止: {}", event.getApplicationContext().getId());
    }

    /**
     * 監聽應用上下文關閉事件
     * 當應用上下文關閉時觸發
     */
    @EventListener
    public void onContextClosed(ContextClosedEvent event) {
        log.info("應用上下文已關閉: {}", event.getApplicationContext().getId());
    }

    /**
     * 監聽方法執行事件
     * 當系統中的方法被執行時觸發
     */
    @EventListener
    public void onMethodExecution(MethodExecutionEvent event) {
        // 避免對於某些方法產生過多的日誌
        String className = event.getClassName();
        String methodName = event.getMethodName();

        // 忽略一些常見的方法
        if (methodName.equals("equals") || methodName.equals("hashCode") ||
                methodName.equals("toString") || methodName.equals("getClass")) {
            return;
        }

        // 忽略 Spring 內部的一些方法
        if (className.startsWith("org.springframework") &&
                (className.contains("Handler") || className.contains("Resolver") ||
                        className.contains("Adapter") || className.contains("Mapper"))) {
            return;
        }

        // 根據方法執行的成功或失敗記錄不同級別的日誌
        if (event.isSuccess()) {
            if (event.getExecutionTime() > 100) { // 只記錄執行時間超過 100ms 的方法
                log.info("[{}] 方法執行: {}.{}，耗時: {} ms",
                        event.getTraceId(), className, methodName, event.getExecutionTime());
            } else {
                log.debug("[{}] 方法執行: {}.{}，耗時: {} ms",
                        event.getTraceId(), className, methodName, event.getExecutionTime());
            }
        } else {
            log.warn("[{}] 方法異常: {}.{}，耗時: {} ms，異常: {}",
                    event.getTraceId(), className, methodName,
                    event.getExecutionTime(), event.getException().getMessage());
        }
    }

}
