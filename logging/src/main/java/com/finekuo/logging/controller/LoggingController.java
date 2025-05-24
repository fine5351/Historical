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
        response.setRocId("rocIdValue");
        response.setRoc_id("roc_id_value");
        response.setAccount_number("account_number_value");
        response.setCredit_card_number("credit_card_number_value");
        return BaseResponse.success(response);
    }

    @PostMapping("/log")
    public BaseResponse<GetLogResponse> getLog(@RequestBody GetLogRequest request) {
        GetLogResponse response = new GetLogResponse();
        response.setInteger(request.getInteger());
        response.setString(request.getString());
        response.setRocId("rocIdValue");
        response.setRoc_id("roc_id_value");
        response.setAccount_number("account_number_value");
        response.setCredit_card_number("credit_card_number_value");
        return BaseResponse.success(response);
    }

    @Data
    public static class GetLogRequest {

        private String string;
        private Integer integer;
        private String rocId;

    }

    @Data
    public static class GetLogResponse {

        private String string;
        private Integer integer;
        private String rocId;
        private String roc_id;
        private String account_number;
        private String credit_card_number;

    }

}
