package com.finekuo.normalcore.advice;

import com.finekuo.normalcore.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@Slf4j
@ControllerAdvice
public class LoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 對所有 request body 生效
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, org.springframework.http.HttpInputMessage inputMessage,
                                MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            log.info("[LoggingRequestBodyAdvice] Request Body: {}", Jsons.toJson(body));
        } catch (Exception e) {
            log.warn("[LoggingRequestBodyAdvice] Failed to log request body", e);
        }
        return body;
    }

}
