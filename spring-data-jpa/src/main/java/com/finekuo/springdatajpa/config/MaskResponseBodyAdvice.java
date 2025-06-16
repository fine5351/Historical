package com.finekuo.springdatajpa.config;

import com.finekuo.springdatajpa.service.EntityMaskService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MaskResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final EntityMaskService entityMaskService;
    private final Gson gson = new Gson();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 可以根据特定条件决定是否应用掩码，例如特定的控制器或方法
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        // 获取请求信息
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String account = servletRequest.getHeader("account");
            String method = servletRequest.getMethod();
            String uri = servletRequest.getRequestURI();

            if (account != null) {
                // 将对象转换为 JsonObject
                JsonObject jsonObject = gson.toJsonTree(body).getAsJsonObject();
                // 应用掩码
                JsonObject maskedJson = entityMaskService.mask(account, method, uri, jsonObject);
                // 将掩码后的 JsonObject 转回原始类型
                return gson.fromJson(maskedJson, body.getClass());
            }
        }

        return body;
    }

}
