package com.finekuo.kafka.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublishEventRequest {

    private String message;
    private LocalDateTime now;

}
