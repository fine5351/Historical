package com.finekuo.logging.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.finekuo.normalcore.dto.response.BaseResponse;
import com.finekuo.normalcore.util.Jsons;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Added for consistency
public class LoggingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getLog_withRequestParams_shouldReturnCorrectResponse() throws Exception {
        Integer testInteger = 123;
        String testString = "test_string_get";

        MvcResult result = mockMvc.perform(get("/logging/log")
                        .param("integer", testInteger.toString())
                        .param("string", testString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<LoggingController.GetLogResponse> response = Jsons.fromJson(
                contentAsString,
                new TypeReference<BaseResponse<LoggingController.GetLogResponse>>() {}
        );

        assertThat(response.getCode()).isEqualTo(0);
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getInteger()).isEqualTo(testInteger);
        assertThat(response.getData().getString()).isEqualTo(testString);
    }

    @Test
    public void postLog_withRequestBody_shouldReturnCorrectResponse() throws Exception {
        LoggingController.GetLogRequest requestBody = new LoggingController.GetLogRequest();
        Integer testInteger = 456;
        String testString = "test_string_post";
        requestBody.setInteger(testInteger);
        requestBody.setString(testString);

        MvcResult result = mockMvc.perform(post("/logging/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jsons.toJson(requestBody)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        BaseResponse<LoggingController.GetLogResponse> response = Jsons.fromJson(
                contentAsString,
                new TypeReference<BaseResponse<LoggingController.GetLogResponse>>() {}
        );

        assertThat(response.getCode()).isEqualTo(0);
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getInteger()).isEqualTo(testInteger);
        assertThat(response.getData().getString()).isEqualTo(testString);
    }
}
