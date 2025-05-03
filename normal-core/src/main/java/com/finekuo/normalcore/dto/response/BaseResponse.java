package com.finekuo.normalcore.dto.response;

import com.finekuo.normalcore.constant.ResponseStatusCode;
import lombok.Data;

@Data
public class BaseResponse<T> {

    private T data;
    private String message;
    private String code;

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setData(data);
        response.setCode(ResponseStatusCode.SUCCESS.getCode());
        response.setMessage("success");
        return response;
    }

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(ResponseStatusCode.SUCCESS.getCode());
        response.setMessage("success");
        return response;
    }

    public static <T> BaseResponse<T> fail(ResponseStatusCode code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code.getCode());
        response.setMessage(message);
        return response;
    }

}
