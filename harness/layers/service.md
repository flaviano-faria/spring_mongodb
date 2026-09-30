# Domain service

`UserService` implements `UserServicePort` and depends only on `UserRepositoryPort` and `User`. Constructor injection via Lombok `@RequiredArgsConstructor`. Do not add `@Service`.

Register a new service with a `@Bean` in `BeanConfiguration`, using the `userService` method as the pattern. The package `com.mongodb.domain.adapter.service` is already on `@ComponentScan`; a new package is not.

Missing data becomes null at this boundary:

```java
return userRepositoryPort.findById(id).orElse(null);
```

Do not call `IUserRepository` or map `UserEntity` here. `save` does not write the generated Mongo id back onto `User`; tests discover the id with `getAllUsers()` or `findById`.

Extend `UserServiceTest` for every behavior change. Keep `// Setup`, `// Execute`, and `// Assert`.
