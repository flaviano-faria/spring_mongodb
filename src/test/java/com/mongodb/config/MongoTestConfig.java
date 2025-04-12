package com.mongodb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
@EnableMongoRepositories(
    basePackages = "com.mongodb.infra.adapters.repository",
    considerNestedRepositories = true
)
@ComponentScan(basePackages = {
    "com.mongodb.domain.adapter.service",
    "com.mongodb.infra.adapters.repository",
    "com.mongodb.infra.configuration"
})
public class MongoTestConfig extends AbstractMongoClientConfiguration {

    private static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:6.0.2");
        mongoDBContainer.start();
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
} 