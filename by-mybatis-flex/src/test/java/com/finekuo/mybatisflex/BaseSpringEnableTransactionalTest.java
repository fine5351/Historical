package com.finekuo.mybatisflex;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = MyBatisFlexApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public abstract class BaseSpringEnableTransactionalTest extends BaseTest {

}
