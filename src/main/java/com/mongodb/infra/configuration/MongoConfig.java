package com.mongodb.infra.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
    basePackages = "com.mongodb.infra.adapters.repository",
    considerNestedRepositories = true
)
public class MongoConfig {
} 