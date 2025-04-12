package com.mongodb.domain.adapter.service;

import com.mongodb.domain.User;
import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.domain.ports.service.UserServicePort;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void createUser(User user) {
        userRepositoryPort.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User getUserById(String id) {

        return userRepositoryPort.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(String id) {
        userRepositoryPort.deleteById(id);
    }
} 