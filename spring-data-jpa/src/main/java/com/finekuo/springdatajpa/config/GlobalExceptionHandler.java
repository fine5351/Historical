package com.finekuo.springdatajpa.config;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error("e = " + e);
    }

    @ExceptionHandler(JsonParseException.class)
    public void handleJsonParseException(JsonParseException e) {
        log.error("e.getMessage() = " + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("e.getMessage() = " + e.getMessage());
    }

}