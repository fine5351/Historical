package com.finekuo.normalcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class BaseControllerTest extends BaseSpringTest {

    @Autowired
    protected MockMvc mockMvc;

}
