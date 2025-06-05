package com.finekuo.logging.aop;

import com.finekuo.logging.event.MethodExecutionEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * 全局方法日誌切面
 * 記錄 HTTP 請求進入 Spring 上下文後經過的所有方法（包括 Spring Framework 內部方法）的輸入參數和返回值
 */
@Aspect
@Component
@Order(1) // 設置高優先級，確保這個切面在其他切面之前執行
@Slf4j
public class HttpRequestLoggingAspect {

    private static final int MAX_PAYLOAD_LOG_LENGTH = 1000; // 最大日誌內容長度
    private static final String TRACE_ID_ATTRIBUTE = "traceId";
    private static final boolean LOG_SPRING_INTERNAL_METHODS = true; // 是否記錄 Spring 內部方法
    private static final boolean LOG_PRIVATE_METHODS = true; // 是否記錄私有方法
    private static final boolean LOG_PROTECTED_METHODS = true; // 是否記錄受保護方法
    private static final boolean LOG_ENTRY_EXIT = true; // 是否記錄方法進入和退出
    private static final boolean LOG_PARAMETERS = true; // 是否記錄方法參數
    private static final boolean LOG_RETURN_VALUES = true; // 是否記錄方法返回值

    @Autowired
    private ApplicationEventPublisher eventPublisher; // 用於發布方法執行事件

    /**
     * 定義切點：所有控制器類中的 public 方法
     */
    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    /**
     * 定義切點：所有 Service 類中的 public 方法
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }

    /**
     * 定義切點：所有 Repository 類中的 public 方法
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *) || within(@org.springframework.data.repository.Repository *) || within(@org.springframework.data.jpa.repository.JpaRepository *)")
    public void repositoryPointcut() {
    }

    /**
     * 定義切點：所有 Component 類中的 public 方法
     */
    @Pointcut("within(@org.springframework.stereotype.Component *)")
    public void componentPointcut() {
    }

    /**
     * 組合切點：所有 Spring 管理的 bean 中的 public 方法
     */
    @Pointcut("(controllerPointcut() || servicePointcut() || repositoryPointcut() || componentPointcut()) && execution(public * *(..))")
    public void springBeanPublicMethodPointcut() {
    }

    /**
     * 定義切點：Spring Framework 內部的所有方法
     */
    @Pointcut("within(org.springframework..*)")
    public void springFrameworkMethodPointcut() {
    }

    /**
     * 定義切點：所有 Spring 管理的 bean 中的 private 方法
     */
    @Pointcut("(controllerPointcut() || servicePointcut() || repositoryPointcut() || componentPointcut()) && execution(private * *(..))")
    public void springBeanPrivateMethodPointcut() {
    }

    /**
     * 定義切點：所有 Spring 管理的 bean 中的 protected 方法
     */
    @Pointcut("(controllerPointcut() || servicePointcut() || repositoryPointcut() || componentPointcut()) && execution(protected * *(..))")
    public void springBeanProtectedMethodPointcut() {
    }

    /**
     * 定義切點：所有方法（包括 public、private、protected、package-private）
     */
    @Pointcut("execution(* *(..))")
    public void allMethodsPointcut() {
    }

    /**
     * 組合切點：所有需要記錄的方法
     * 包括：Spring 管理的 bean 的所有方法、Spring Framework 內部方法
     */
    @Pointcut("springBeanPublicMethodPointcut() || springBeanPrivateMethodPointcut() || springBeanProtectedMethodPointcut() || springFrameworkMethodPointcut()")
    public void allTrackedMethodsPointcut() {
    }

    /**
     * 環繞通知：記錄方法輸入參數和返回值
     */
    @Around("allTrackedMethodsPointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // 獲取當前請求的 traceId，如果不存在則創建一個新的
        String traceId = getOrCreateTraceId();

        // 獲取方法簽名
        String className = "";
        String methodName = "";
        Method method = null;

        try {
            if (joinPoint.getSignature() instanceof MethodSignature) {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                method = signature.getMethod();
                className = method.getDeclaringClass().getSimpleName();
                methodName = method.getName();
            } else {
                // 處理非 MethodSignature 的情況
                className = joinPoint.getTarget().getClass().getSimpleName();
                methodName = joinPoint.getSignature().getName();
            }
        } catch (Exception e) {
            // 如果無法獲取方法信息，使用備用方案
            className = joinPoint.getTarget() != null ?
                    joinPoint.getTarget().getClass().getSimpleName() : "UnknownClass";
            methodName = joinPoint.getSignature() != null ?
                    joinPoint.getSignature().getName() : "unknownMethod";
        }

        // 獲取方法參數
        Object[] args = joinPoint.getArgs();
        String params = formatParams(args);

        // 判斷是否應該記錄這個方法
        boolean shouldLog = shouldLogMethod(className, methodName);

        // 記錄方法開始執行
        if (shouldLog && LOG_ENTRY_EXIT) {
            if (LOG_PARAMETERS) {
                log.info("[{}] >>> 開始執行: {}.{}({})", traceId, className, methodName, params);
            } else {
                log.info("[{}] >>> 開始執行: {}.{}", traceId, className, methodName);
            }
        }

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            // 執行目標方法
            result = joinPoint.proceed();

            // 記錄方法執行結果
            long executionTime = System.currentTimeMillis() - startTime;

            if (shouldLogMethod(className, methodName) && LOG_ENTRY_EXIT) {
                if (LOG_RETURN_VALUES) {
                    log.info("[{}] <<< 執行完成: {}.{}，耗時: {} ms，返回值: {}",
                            traceId, className, methodName, executionTime, formatReturnValue(result));
                } else {
                    log.info("[{}] <<< 執行完成: {}.{}，耗時: {} ms",
                            traceId, className, methodName, executionTime);
                }
            }

            // 發布方法執行成功事件
            publishMethodExecutionEvent(className, methodName, args, result, executionTime, null);

            return result;
        } catch (Throwable throwable) {
            // 記錄方法執行異常
            long executionTime = System.currentTimeMillis() - startTime;

            // 異常始終記錄，無論方法過濾設置如何
            log.error("[{}] !!! 執行異常: {}.{}，耗時: {} ms，異常: {}",
                    traceId, className, methodName, executionTime, throwable.getMessage(), throwable);

            // 發布方法執行異常事件
            publishMethodExecutionEvent(className, methodName, args, null, executionTime, throwable);

            throw throwable;
        }
    }

    /**
     * 獲取當前請求的 traceId，如果不存在則創建一個新的
     */
    private String getOrCreateTraceId() {
        try {
            Object requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String traceId = (String) request.getAttribute(TRACE_ID_ATTRIBUTE);

                if (traceId == null) {
                    traceId = UUID.randomUUID().toString().replace("-", "");
                    request.setAttribute(TRACE_ID_ATTRIBUTE, traceId);
                    log.debug("為請求創建新的 traceId: {}", traceId);
                }

                return traceId;
            } else if (requestAttributes != null) {
                // 處理其他類型的 RequestAttributes
                log.debug("發現非 ServletRequestAttributes 類型的請求上下文: {}", requestAttributes.getClass().getName());
            }
        } catch (Exception e) {
            log.warn("獲取請求上下文失敗: {}", e.getMessage());
        }

        // 如果無法從請求上下文獲取，則生成一個新的 traceId
        String traceId = UUID.randomUUID().toString().replace("-", "");
        log.debug("無法從請求上下文獲取 traceId，創建新的: {}", traceId);
        return traceId;
    }

    /**
     * 格式化方法參數
     */
    private String formatParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        // 限制參數的數量，防止日誌過大
        final int MAX_ARGS_TO_LOG = 10;
        final int argsToLog = Math.min(args.length, MAX_ARGS_TO_LOG);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < argsToLog; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(formatValue(args[i]));
        }

        if (args.length > MAX_ARGS_TO_LOG) {
            sb.append(", ... (and ").append(args.length - MAX_ARGS_TO_LOG).append(" more)");
        }

        return sb.toString();
    }

    /**
     * 格式化返回值
     */
    private String formatReturnValue(Object result) {
        if (result == null) {
            return "null";
        }
        return formatValue(result);
    }

    /**
     * 格式化單個值
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }

        try {
            String resultStr;

            // 處理各種特殊類型
            if (value instanceof byte[]) {
                byte[] byteArray = (byte[]) value;
                resultStr = String.format("byte[%d]", byteArray.length);
            } else if (value instanceof char[]) {
                char[] charArray = (char[]) value;
                resultStr = String.format("char[%d]", charArray.length);
            } else if (value instanceof Object[]) {
                Object[] objArray = (Object[]) value;
                resultStr = String.format("%s[%d]", value.getClass().getComponentType().getSimpleName(), objArray.length);
            } else if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                resultStr = String.format("%s(size=%d)", value.getClass().getSimpleName(), collection.size());
            } else if (value instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) value;
                resultStr = String.format("%s(size=%d)", value.getClass().getSimpleName(), map.size());
            } else if (value instanceof InputStream || value instanceof OutputStream) {
                resultStr = value.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(value));
            } else if (value instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) value;
                resultStr = String.format("HttpServletRequest(method=%s, uri=%s)", request.getMethod(), request.getRequestURI());
            } else if (value instanceof HttpServletResponse) {
                resultStr = "HttpServletResponse@" + Integer.toHexString(System.identityHashCode(value));
            } else if (value instanceof Class) {
                resultStr = "Class<" + ((Class<?>) value).getName() + ">";
            } else if (value.getClass().getName().startsWith("java.") ||
                    value.getClass().getName().startsWith("javax.") ||
                    value.getClass().getName().startsWith("jakarta.") ||
                    value.getClass().getName().startsWith("org.springframework.")) {
                // 對於標準庫類型，使用簡化表示
                resultStr = value.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(value));
            } else {
                // 其他類型使用 toString
                resultStr = value.toString();
            }

            // 限制日誌內容長度
            if (resultStr.length() > MAX_PAYLOAD_LOG_LENGTH) {
                return resultStr.substring(0, MAX_PAYLOAD_LOG_LENGTH) + "... (總長度: " + resultStr.length() + ")";
            }

            return resultStr;
        } catch (Throwable t) {
            // 處理格式化過程中的任何異常
            return value.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(value)) + "(格式化異常: " + t.getMessage() + ")";
        }
    }

    /**
     * 判斷是否應該記錄這個方法
     */
    private boolean shouldLogMethod(String className, String methodName) {
        // 忽略一些會產生大量日誌的常見方法
        if (methodName.equals("equals") || methodName.equals("hashCode") ||
                methodName.equals("toString") || methodName.equals("getClass")) {
            return false;
        }

        // 忽略一些常見的 getter 和 setter 方法
        if ((methodName.startsWith("get") || methodName.startsWith("set") || methodName.startsWith("is")) &&
                methodName.length() > 3) {
            return false;
        }

        // 忽略一些特定的 Spring 內部方法，這些方法會產生大量日誌
        if (className.startsWith("Cglib") || className.contains("$$") ||
                className.equals("HandlerMethod") || className.equals("HandlerExecutionChain") ||
                className.equals("RequestMappingHandlerMapping") || className.equals("RequestMappingHandlerAdapter") ||
                className.equals("DispatcherServlet") && methodName.equals("doService") ||
                className.equals("HttpRequestHandlerAdapter") || className.equals("SimpleControllerHandlerAdapter")) {
            return false;
        }

        return true;
    }

    /**
     * 發布方法執行事件
     */
    private void publishMethodExecutionEvent(String className, String methodName, Object[] args,
                                             Object result, long executionTime, Throwable exception) {
        try {
            // 檢查是否在 HTTP 請求上下文中
            if (RequestContextHolder.getRequestAttributes() != null) {
                MethodExecutionEvent event = new MethodExecutionEvent(this);
                event.setClassName(className);
                event.setMethodName(methodName);
                event.setParameters(args);
                event.setResult(result);
                event.setExecutionTime(executionTime);
                event.setException(exception);
                event.setTraceId(getOrCreateTraceId());

                // 發布事件
                eventPublisher.publishEvent(event);
            }
        } catch (Exception e) {
            log.warn("發布方法執行事件失敗: {}", e.getMessage(), e);
        }
    }

}
