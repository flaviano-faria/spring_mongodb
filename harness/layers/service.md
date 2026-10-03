# Domain service

`UserService` implements `UserServicePort` and depends on `UserRepositoryPort` and `User`. Constructor injection via Lombok `@RequiredArgsConstructor`.

Do not add `@Service`, `@Component`, or `@Repository` to `UserService`. Spring creates this object only from the `userService` `@Bean` in `BeanConfiguration`. The package `com.mongodb.domain.adapter.service` is on `SpringMongoApplication`'s `@ComponentScan`, and that scan does not register `UserService` because the class has no stereotype.

A new type in this already-scanned package can use `@Component` or `@Service` and will be registered. Use a `@Bean` in `BeanConfiguration` when the class should stay free of stereotypes, which is the `UserService` pattern. A new package must be added to `SpringMongoApplication`'s `@ComponentScan`. See `layers/bootstrap.md` and wiring constraint 2 in `AGENTS.md`.

Missing data becomes null at this boundary:

```java
return userRepositoryPort.findById(id).orElse(null);
```

Do not call `IUserRepository` or map `UserEntity` here. `save` does not write the generated Mongo id back onto `User`. That contract is wiring constraint 5 in `AGENTS.md`.

Extend `UserServiceTest` for every behavior change. Test style is the unit test policy in `AGENTS.md`.
