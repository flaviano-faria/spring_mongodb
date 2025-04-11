package com.mongodb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@TestConfiguration
@EnableMongoRepositories(basePackages = "com.mongodb.infra.adapters.repository")
@ComponentScan(basePackages = {
    "com.mongodb.domain.adapter.service",
    "com.mongodb.infra.adapters.repository"
})
public class MongoTestConfig {
} 