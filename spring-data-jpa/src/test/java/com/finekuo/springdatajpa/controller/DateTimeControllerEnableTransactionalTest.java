package com.finekuo.springdatajpa.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.finekuo.normalcore.BaseControllerEnableTransactionalTest;
import com.finekuo.normalcore.dto.response.BaseResponse;
import com.finekuo.normalcore.util.Jsons;
import com.finekuo.springdatajpa.dto.request.LocalDateTimeRequest;
import com.finekuo.springdatajpa.dto.response.TimeResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class DateTimeControllerEnableTransactionalTest extends BaseControllerEnableTransactionalTest {

    @Test
    public void testPrintDateTime() throws Exception {
        LocalDateTimeRequest requestBody = new LocalDateTimeRequest();
        requestBody.setLocalDateTime(LocalDateTime.now()); // Populate the request body

        MvcResult result = mockMvc.perform(post("/datetime/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(requestBody))) // Add the request body
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<TimeResponse> apiResponse = Jsons.fromJson(contentAsString, new TypeReference<BaseResponse<TimeResponse>>() {

        });
        TimeResponse timeResponse = apiResponse.getData();

        assertThat(timeResponse).isNotNull();
        assertThat(timeResponse.getLocalDate()).isNotNull();
        assertThat(timeResponse.getLocalDateTime()).isNotNull();
        assertThat(timeResponse.getOffsetDateTime().getYear()).isGreaterThanOrEqualTo(LocalDate.now().getYear());
        assertThat(timeResponse.getOffsetDateTime().getMonthValue()).isBetween(1, 12);
        assertThat(timeResponse.getOffsetDateTime().getDayOfMonth()).isBetween(1, 31);
        assertThat(timeResponse.getOffsetDateTime().getHour()).isBetween(0, 23);
        assertThat(timeResponse.getOffsetDateTime().getMinute()).isBetween(0, 59);
        assertThat(timeResponse.getOffsetDateTime().getSecond()).isBetween(0, 59);
        // Nanos can be 0, so just check for not null if it's an Integer, or non-negative if primitive
        assertThat(timeResponse.getOffsetDateTime().getNano()).isGreaterThanOrEqualTo(0);

        assertThat(timeResponse.getOffsetDateTime().getDayOfWeek()).isNotNull();
        assertThat(timeResponse.getOffsetDateTime().getDayOfYear()).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(366); // Handles leap years
        assertThat(timeResponse.getOffsetDateTime().getMonthValue()).isBetween(1, 12);

        // Check if the date and time parts from the response can be parsed back to LocalDate and LocalTime
        // This provides a stronger guarantee that the values are reasonable.
        assertThat(timeResponse.getLocalDate()).isEqualTo(LocalDate.now()); // Directly compare LocalDate part
        // For time, it's harder due to rapid changes. We can check components.
        LocalTime responseTime = timeResponse.getLocalDateTime().toLocalTime(); // Use localDateTime for LocalTime part
        LocalTime now = LocalTime.now();
        assertThat(responseTime.getHour()).isEqualTo(now.getHour());
        // Minute and second might change between the controller call and this assertion.
        // So, we check if they are within a reasonable range, e.g., the current or previous minute/second.
        // A common strategy is to check if the response minute is one of [now.getMinute(), (now.getMinute() - 1 + 60) % 60]
        // This handles the case where the minute might have just ticked over.
        int currentMinute = now.getMinute();
        int previousMinute = (currentMinute == 0) ? 59 : currentMinute - 1;
        assertThat(responseTime.getMinute()).isIn(currentMinute, previousMinute);
    }

}
