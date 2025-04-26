package com.jkos.hackathon.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocalDateTimeRequest {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime localDateTime;

}
