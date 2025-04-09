package com.mongodb.infra.adapters.repository;

import com.mongodb.domain.User;
import com.mongodb.domain.ports.repository.UserRepositoryPort;
import com.mongodb.infra.adapters.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository implements UserRepositoryPort {

    private final RestClient.Builder builder;
    private IUserRepository iUserRepository;

    public UserRepository(IUserRepository iUserRepository, RestClient.Builder builder) {
        this.iUserRepository = iUserRepository;
        this.builder = builder;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.builder()
                .document(user.getDocument())
                .name(user.getName())
                .age(user.getAge()).build();
        return iUserRepository.save(userEntity);
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {

    }
}