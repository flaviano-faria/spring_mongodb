package com.mongodb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@TestConfiguration
public class TestConfig {

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.2");

    static {
        mongoDBContainer.start();
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "test");
    }

    public static String getMongoUri() {
        return mongoDBContainer.getReplicaSetUrl();
    }
} 