# Ports

Ports are plain interfaces. Name them `*Port`. No `@Service`, `@Repository`, `@Component`, Spring Data types, or HTTP types.

- `UserServicePort` is the inbound port. Controllers and `UserService` are the only callers. Read methods return `User` or `List<User>`. Write methods return `void`.
- `UserRepositoryPort` is the outbound port. `findById` returns `Optional<User>`. `save` returns `void` and does not promise a generated id.

A new use case adds the method here and on `UserService` together. A new persistence operation adds it here and on `UserRepository` together. Update the tests of every implementation. Do not put business logic in the interface.
