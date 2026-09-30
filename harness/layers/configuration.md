# Configuration

`BeanConfiguration` registers application services. A new service that is not stereotype-scanned gets a `@Bean` method next to `userService`, returning the port type:

```java
@Bean
@Primary
public UserServicePort userService(UserRepositoryPort userRepositoryPort) {
    return new UserService(userRepositoryPort);
}
```

`MongoConfig` enables Spring Data only for `com.mongodb.infra.adapters.repository`. A repository in another package will not be detected.

`application.properties` sets `spring.data.mongodb.host` and `port`. There is no `uri` and no `database`, so Boot uses the `test` database. `MongoProperties` does not build the `MongoClient`; leave Boot auto-config in place unless the task is to replace it.

`UserServiceTest` loads this configuration through the full Spring context. It must still pass after a config change. Add a dedicated test only when a class here gains logic of its own.
