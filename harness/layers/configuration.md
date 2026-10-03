# Configuration

This layer is `com.mongodb.infra.configuration` and `src/main/resources/application.properties`.

`BeanConfiguration` registers application services. `UserService` is registered here, not by component scan. A new service that is not a scanned stereotype gets a `@Bean` method next to `userService`, returning the port type:

```java
@Bean
@Primary
public UserServicePort userService(UserRepositoryPort userRepositoryPort) {
    return new UserService(userRepositoryPort);
}
```

`MongoConfig` enables Spring Data only for `com.mongodb.infra.adapters.repository`. A repository in another package will not be detected.

What Boot actually connects with is `application.properties`: `spring.data.mongodb.host`, `spring.data.mongodb.port`, and `server.servlet.context-path`. There is no `uri` and no `database` property, so the database is `test`. Details are the runtime config section in `AGENTS.md`.

`MongoProperties` declares `uri`, `database`, `host`, `port`, `username`, `password`, and `authenticationDatabase`. It does not build a `MongoClient`. Setting those fields does not change the connection. Leave Boot auto-config in place unless the task is to replace it.

`UserServiceTest` loads this configuration through the full Spring context. It must still pass after a config change. Add a dedicated test only when a class here gains logic of its own.
