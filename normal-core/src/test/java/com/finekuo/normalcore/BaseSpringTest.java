package com.finekuo.normalcore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = TestApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class BaseSpringTest extends BaseTest {

}
