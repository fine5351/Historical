package com.finekuo.logging.controller;

import com.finekuo.logging.controller.LoggingController.GetLogRequest;
import com.finekuo.normalcore.BaseControllerEnableTransactionalTest;
import com.finekuo.normalcore.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class LoggingControllerEnableTransactionalTest extends BaseControllerEnableTransactionalTest {

    @Test
    void getLog_shouldReturnParameters() throws Exception {
        int testInteger = 123;
        String testString = "testStringForGet";

        mockMvc.perform(get("/logging/log")
                        .param("integer", String.valueOf(testInteger))
                        .param("string", testString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
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
                        .content(Jsons.toJson(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.integer", is(testInteger)))
                .andExpect(jsonPath("$.data.string", is(testString)));
    }

}
