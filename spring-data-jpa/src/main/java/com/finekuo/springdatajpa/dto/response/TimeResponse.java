package com.finekuo.springdatajpa.dto.response;

import java.time.*;
import java.util.Date;

public record TimeResponse(Date date, LocalDate localDate, LocalDateTime localDateTime, ZonedDateTime zonedDateTime, Instant instant, OffsetDateTime offsetDateTime) {

}
