package com.finekuo.normalcore.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.finekuo.normalcore.constant.ResponseStatusCode;
import com.finekuo.normalcore.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception occurred: ", e);
        BaseResponse<Void> response = BaseResponse.fail(ResponseStatusCode.FAILURE, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleJsonParseException(JsonParseException e) {
        log.warn("JSON Parse exception: ", e);
        BaseResponse<Void> response = BaseResponse.fail(ResponseStatusCode.BAD_REQUEST, "JSON parsing error: " + e.getOriginalMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HTTP message not readable: ", e);
        BaseResponse<Void> response = BaseResponse.fail(ResponseStatusCode.BAD_REQUEST, "HTTP message not readable: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument exception: ", e);
        BaseResponse<Void> response;
        HttpStatus status;
        if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
            response = BaseResponse.fail(ResponseStatusCode.NOT_FOUND, e.getMessage());
            status = HttpStatus.NOT_FOUND;
        } else {
            response = BaseResponse.fail(ResponseStatusCode.BAD_REQUEST, e.getMessage());
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(response, status);
    }
    
    @ExceptionHandler(JpaSystemException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<Void>> handleJpaSystemException(JpaSystemException e) {
        log.error("JPA System Exception: ", e);
        BaseResponse<Void> response = BaseResponse.fail(ResponseStatusCode.FAILURE, "A database error occurred: " + e.getRootCause().getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // It might be useful to add handlers for MethodArgumentNotValidException for @Valid failures too
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // @ResponseBody
    // public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getAllErrors().forEach((error) -> {
    //         String fieldName = ((FieldError) error).getField();
    //         String errorMessage = error.getDefaultMessage();
    //         errors.put(fieldName, errorMessage);
    //     });
    //     BaseResponse<Object> response = BaseResponse.fail(ResponseStatusCode.BAD_REQUEST, "Validation failed", errors);
    //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    // }

}