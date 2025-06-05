package com.finekuo.logging.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 方法執行事件
 * 在 HTTP 請求經過 Spring 上下文中的方法時觸發
 */
@Getter
@Setter
public class MethodExecutionEvent extends ApplicationEvent {

    private String className;         // 類名
    private String methodName;        // 方法名
    private Object[] parameters;      // 方法參數
    private Object result;            // 方法返回值
    private long executionTime;       // 執行時間（毫秒）
    private Throwable exception;      // 異常信息（如果有）
    private String traceId;           // 追蹤 ID

    public MethodExecutionEvent(Object source) {
        super(source);
    }

    public MethodExecutionEvent(Object source, String className, String methodName, Object[] parameters, Object result, long executionTime, Throwable exception, String traceId) {
        super(source);
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.result = result;
        this.executionTime = executionTime;
        this.exception = exception;
        this.traceId = traceId;
    }

    /**
     * 檢查方法執行是否成功
     */
    public boolean isSuccess() {
        return exception == null;
    }

}
