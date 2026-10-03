# Ports

Ports are plain interfaces. Name them `*Port`. No `@Service`, `@Repository`, `@Component`, Spring Data types, or HTTP types.

- `UserServicePort` is the inbound port. Read methods return `User` or `List<User>`. Write methods return `void`.
- `UserRepositoryPort` is the outbound port. `findById` returns `Optional<User>`. `save` returns `void` and does not promise a generated id. The port must not mention `UserEntity` or `MongoRepository`.

A new use case adds the method here and on `UserService` together. A new persistence operation adds it here and on `UserRepository` together. A custom Mongo query is a method on `IUserRepository`, not on the port. See `layers/persistence-repository.md`.

Update the tests of every implementation, per the unit test policy in `AGENTS.md`. Do not put business logic in the interface.
