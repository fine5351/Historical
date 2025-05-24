package com.finekuo.springdatajpa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeResponse {

    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private ZonedDateTime zonedDateTime;
    private Instant instant;
    private OffsetDateTime offsetDateTime;
}
