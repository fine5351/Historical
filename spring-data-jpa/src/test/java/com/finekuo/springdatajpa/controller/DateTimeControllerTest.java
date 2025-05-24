package com.finekuo.springdatajpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.springdatajpa.dto.response.TimeResponse; // Corrected import from previous subtask
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles; // Import for ActiveProfiles
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Added for E2E configuration
public class DateTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // No @MockBean annotations were present, so none to remove.

    @Test
    public void testPrintDateTime() throws Exception {
        LocalDateTimeRequest requestBody = new LocalDateTimeRequest();
        requestBody.setLocalDateTime(LocalDateTime.now()); // Populate the request body

        MvcResult result = mockMvc.perform(post("/datetime/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))) // Add the request body
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        TimeResponse timeResponse = objectMapper.readValue(contentAsString, TimeResponse.class);

        // Assertions for the TimeResponse object
        // Due to LocalDateTime.now(), exact matches are difficult.
        // Instead, we'll check for non-null values and basic patterns.
        assertThat(timeResponse).isNotNull();
        assertThat(timeResponse.localDate()).isNotNull(); // Changed to record accessor
        assertThat(timeResponse.localDateTime()).isNotNull(); // Changed to record accessor
        assertThat(timeResponse.offsetDateTime().getYear()).isGreaterThanOrEqualTo(LocalDate.now().getYear()); // Example: Using offsetDateTime for detailed checks
        assertThat(timeResponse.offsetDateTime().getMonthValue()).isBetween(1, 12);
        assertThat(timeResponse.offsetDateTime().getDayOfMonth()).isBetween(1, 31);
        assertThat(timeResponse.offsetDateTime().getHour()).isBetween(0, 23);
        assertThat(timeResponse.offsetDateTime().getMinute()).isBetween(0, 59);
        assertThat(timeResponse.offsetDateTime().getSecond()).isBetween(0, 59);
        // Nanos can be 0, so just check for not null if it's an Integer, or non-negative if primitive
        assertThat(timeResponse.offsetDateTime().getNano()).isGreaterThanOrEqualTo(0);

        assertThat(timeResponse.offsetDateTime().getDayOfWeek()).isNotNull();
        assertThat(timeResponse.offsetDateTime().getDayOfYear()).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(366); // Handles leap years
        assertThat(timeResponse.offsetDateTime().getMonthValue()).isBetween(1,12);

        // Check if the date and time parts from the response can be parsed back to LocalDate and LocalTime
        // This provides a stronger guarantee that the values are reasonable.
        assertThat(timeResponse.localDate()).isEqualTo(LocalDate.now()); // Directly compare LocalDate part
        // For time, it's harder due to rapid changes. We can check components.
        LocalTime responseTime = timeResponse.localDateTime().toLocalTime(); // Use localDateTime for LocalTime part
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
