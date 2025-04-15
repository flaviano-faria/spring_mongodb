package com.mongodb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {
    "com.mongodb.infra.configuration",
    "com.mongodb.infra.adapters.repository",
    "com.mongodb.controller",
    "com.mongodb.domain.adapter.service"

})
public class SpringMongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMongoApplication.class, args);
    }
} 