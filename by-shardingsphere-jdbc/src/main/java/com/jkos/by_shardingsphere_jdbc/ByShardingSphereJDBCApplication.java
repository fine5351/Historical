package com.jkos.by_shardingsphere_jdbc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Hackathon MyBatis-Flex", version = "1.0", description = "Hackathon MyBatis-Flex Demo"))
public class ByShardingSphereJDBCApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(ByShardingSphereJDBCApplication.class, args);
    }

}