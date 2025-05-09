package com.mongodb.domain.ports.service;

import com.mongodb.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserServicePort {
    void createUser(User user);
    List<User> getAllUsers();
    User getUserById(String id);
    void deleteUser(String id);
} 