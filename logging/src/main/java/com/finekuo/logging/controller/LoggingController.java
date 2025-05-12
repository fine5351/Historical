package com.finekuo.logging.controller;

import com.finekuo.normalcore.dto.response.BaseResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

    @GetMapping("/log")
    public BaseResponse<GetLogResponse> getLog(@RequestParam Integer integer,
                                               @RequestParam String string) {
        GetLogResponse response = new GetLogResponse();
        response.setInteger(integer);
        response.setString(string);
        return BaseResponse.success(response);
    }

    @PostMapping("/log")
    public BaseResponse<GetLogResponse> getLog(@RequestBody GetLogRequest request) {
        GetLogResponse response = new GetLogResponse();
        response.setInteger(request.getInteger());
        response.setString(request.getString());
        return BaseResponse.success(response);
    }

    @Data
    public static class GetLogRequest {

        private String string;
        private Integer integer;

    }

    @Data
    public static class GetLogResponse {

        private String string;
        private Integer integer;

    }

}
