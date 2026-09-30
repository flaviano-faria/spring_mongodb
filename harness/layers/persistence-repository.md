# Persistence adapter

`UserRepository` implements `UserRepositoryPort` and is the only production class that calls `IUserRepository`. Map with `UserEntity.fromUser` on the way in and `UserEntity::toUser` on the way out.

`save` stores the entity and discards the saved instance, so the generated id is not copied back onto `User`. Keep that contract unless the task changes it.

```java
UserEntity savedEntity = iUserRepository.save(userEntity);
```

New Spring Data interfaces live in this package. `MongoConfig` enables repositories only for `com.mongodb.infra.adapters.repository`. Annotate the adapter with `@Repository`.

On the first change, add `UserRepositoryTest` with the same Testcontainers setup as `UserServiceTest`.
