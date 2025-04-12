package com.mongodb.infra.adapters.repository;

import com.mongodb.infra.adapters.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends MongoRepository<UserEntity, String> {
} 