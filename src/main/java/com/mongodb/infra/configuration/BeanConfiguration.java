package com.mongodb.infra.configuration;

import com.mongodb.domain.adapter.service.UserService;
import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.domain.ports.service.UserServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {

    @Bean
    UserServicePort userService(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);

    }

}
