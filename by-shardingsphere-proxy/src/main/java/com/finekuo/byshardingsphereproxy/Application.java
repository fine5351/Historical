package com.finekuo.byshardingsphereproxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "com.finekuo"
})
@OpenAPIDefinition(info = @Info(title = "Hackathon ShardingSphere Proxy", version = "1.0", description = "Hackathon ShardingSphere Proxy Demo"))
public class Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(Application.class, args);
    }

}