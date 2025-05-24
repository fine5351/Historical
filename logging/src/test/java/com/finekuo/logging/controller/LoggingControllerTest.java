package com.finekuo.logging.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finekuo.logging.controller.LoggingController.GetLogRequest; // Inner class
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class LoggingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getLog_shouldReturnParameters() throws Exception {
        int testInteger = 123;
        String testString = "testStringForGet";

        mockMvc.perform(get("/logging/log")
                        .param("integer", String.valueOf(testInteger))
                        .param("string", testString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.integer", is(testInteger)))
                .andExpect(jsonPath("$.data.string", is(testString)));
    }

    @Test
    void postLog_shouldReturnRequestObject() throws Exception {
        GetLogRequest request = new GetLogRequest();
        int testInteger = 456;
        String testString = "testStringForPost";
        request.setInteger(testInteger);
        request.setString(testString);

        mockMvc.perform(post("/logging/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.integer", is(testInteger)))
                .andExpect(jsonPath("$.data.string", is(testString)));
    }
}
