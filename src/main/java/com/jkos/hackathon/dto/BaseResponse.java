package com.jkos.hackathon.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private T data;
    private String message;
    private String code;

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setData(data);
        response.setCode("0000");
        response.setMessage("success");
        return response;
    }

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode("0000");
        response.setMessage("success");
        return response;
    }

    public static <T> BaseResponse<T> fail(String code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

}
