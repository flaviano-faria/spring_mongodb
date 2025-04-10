package com.mongodb.domain.ports.repository;

import com.mongodb.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    void save(User user);
    List<User> findAll();
    Optional<User> findById(String id);
    void deleteById(String id);
    void deleteAll();
}