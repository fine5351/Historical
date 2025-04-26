package com.jkos.hackathon.controller;

import com.jkos.hackathon.dto.request.LocalDateTimeRequest;
import com.jkos.hackathon.dto.response.BaseResponse;
import com.jkos.hackathon.dto.response.TimeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequestMapping("/datetime")
@RestController
@Slf4j
public class DateTimeController {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Taipei");

    @PostMapping("/print")
    public BaseResponse<TimeResponse> print(@RequestBody LocalDateTimeRequest request) {
        return BaseResponse.success(new TimeResponse(new Date(),
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now().atZone(DEFAULT_ZONE_ID),
                LocalDateTime.now().toInstant(DEFAULT_ZONE_ID.getRules().getOffset(LocalDateTime.now())),
                LocalDateTime.now().atOffset(DEFAULT_ZONE_ID.getRules().getOffset(LocalDateTime.now()))));
    }

}
