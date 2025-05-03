package com.finekuo.normalcore.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.finekuo.normalcore.constant.ResponseStatusCode;
import com.finekuo.normalcore.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleException(Exception e) {
        log.error(null, e);
        return BaseResponse.fail(ResponseStatusCode.FAILURE, e.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public BaseResponse<Void> handleJsonParseException(JsonParseException e) {
        log.warn(null, e);
        return BaseResponse.fail(ResponseStatusCode.FAILURE, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(null, e);
        return BaseResponse.fail(ResponseStatusCode.FAILURE, e.getMessage());
    }

}