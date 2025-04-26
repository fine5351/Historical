package com.jkos.mybatisflex.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jkos.mybatisflex.mapper")
//@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisFlexConfig {
    // Let Spring Boot auto-configuration handle the setup

    @Bean
    public DateTimeFillHandler dateTimeFillHandler() {
        return new DateTimeFillHandler();
    }

}
