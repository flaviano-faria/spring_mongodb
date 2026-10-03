# Persistence adapter

This package holds two types. `MongoConfig` enables Spring Data only here.

## UserRepository

`UserRepository` implements `UserRepositoryPort` and is the only production class that calls `IUserRepository`. Annotate it with `@Repository`. Map with `UserEntity.fromUser` on the way in and `UserEntity::toUser` on the way out.

`save` stores the entity and discards the saved instance, so the generated id is not copied back onto `User`. Keep that contract unless the task changes it. See wiring constraint 5 in `AGENTS.md`.

```java
UserEntity savedEntity = iUserRepository.save(userEntity);
```

## IUserRepository

`IUserRepository` is the Spring Data interface:

```java
@Repository
public interface IUserRepository extends MongoRepository<UserEntity, String> {
}
```

Leave it in `com.mongodb.infra.adapters.repository`. A repository in another package is not detected. It must use `UserEntity`, not `User`.

A custom Mongo query is a method on `IUserRepository`. Do not add Spring Data types to `UserRepositoryPort`. `UserRepository` implements that method by calling the new interface method and mapping the result with `toUser`.

On the first change to either type, add `UserRepositoryTest` with the same Testcontainers setup as `UserServiceTest`. The class-to-test map is in `AGENTS.md`.
