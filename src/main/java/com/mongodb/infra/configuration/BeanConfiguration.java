package com.mongodb.infra.configuration;

import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.domain.ports.service.UserServicePort;
import com.mongodb.domain.adapter.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfiguration {

    @Bean
    @Primary
    public UserServicePort userService(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);
    }
}
