package com.pkuo.shardingsphereproxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Hackathon ShardingSphere Proxy", version = "1.0", description = "Hackathon ShardingSphere Proxy Demo"))
public class ShardingSphereProxyApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(ShardingSphereProxyApplication.class, args);
    }

}