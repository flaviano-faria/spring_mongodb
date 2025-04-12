package com.mongodb.infra.adapters.repository;

import com.mongodb.domain.User;
import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.infra.adapters.entity.UserEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository implements UserRepositoryPort {
    private final IUserRepository iUserRepository;

    public UserRepository(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntity.fromUser(user);
        UserEntity savedEntity = iUserRepository.save(userEntity);
    }

    @Override
    public List<User> findAll() {
        return iUserRepository.findAll().stream()
                .map(UserEntity::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        return iUserRepository.findById(id)
                .map(UserEntity::toUser);
    }

    @Override
    public void deleteById(String id) {
        iUserRepository.deleteById(id);
    }

    public void deleteAll() {
        iUserRepository.deleteAll();
    }
}