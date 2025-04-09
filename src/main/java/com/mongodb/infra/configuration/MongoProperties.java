package com.mongodb.infra.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {
    private String uri;
    private String database;
    private String host;
    private int port;
    private String username;
    private String password;
    private String authenticationDatabase;
} 