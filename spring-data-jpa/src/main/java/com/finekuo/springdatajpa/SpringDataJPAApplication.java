package com.finekuo.springdatajpa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Hackathon", version = "1.0", description = "Hackathon"))
public class SpringDataJPAApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(SpringDataJPAApplication.class, args);
    }

}