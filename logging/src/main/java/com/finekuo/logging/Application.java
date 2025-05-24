package com.finekuo.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.finekuo")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
