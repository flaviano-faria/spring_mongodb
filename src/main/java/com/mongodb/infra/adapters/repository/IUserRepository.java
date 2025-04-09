package com.mongodb.infra.adapters.repository;

import com.mongodb.infra.adapters.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<UserEntity, String> {
} 