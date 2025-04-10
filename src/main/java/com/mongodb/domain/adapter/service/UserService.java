package com.mongodb.domain.adapter.service;

import com.mongodb.domain.User;
import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.domain.ports.service.UserServicePort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServicePort {
    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void createUser(User user) {
        userRepositoryPort.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryPort.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepositoryPort.findById(id);
    }

    @Override
    public void deleteUser(String id) {
        userRepositoryPort.deleteById(id);
    }
} 