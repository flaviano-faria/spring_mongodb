# AGENTS.md — spring_mongodb

Guidance for AI agents working in this repository. Prefer this file over README when they diverge (README is partially outdated).

## What this project is

Spring Boot **3.2.0** / **Java 17** demo: User CRUD REST API on MongoDB using a **hexagonal (ports & adapters)** layout. Single aggregate: `User` (`id`, `document`, `name`, `age`). Packaging is `war`; still runnable with `mvn spring-boot:run`.

**Stack:** Spring Web, Spring Data MongoDB, Validation starter (unused), Lombok, JUnit 5, Testcontainers MongoDB.

## Architecture (must preserve)

```
UserController  →  UserServicePort  →  UserService  →  UserRepositoryPort  →  UserRepository  →  IUserRepository  →  MongoDB
(driving)         (inbound port)      (impl)          (outbound port)        (adapter)          (Spring Data)
```

| Layer | Package | Role |
|-------|---------|------|
| Bootstrap | `com.mongodb.app` | `SpringMongoApplication` |
| HTTP | `com.mongodb.controller` | REST adapters |
| Domain model | `com.mongodb.domain` | `User` |
| Ports | `com.mongodb.domain.ports.service` / `...repository` | Interfaces |
| Service impl | `com.mongodb.domain.adapter.service` | `UserService` (not `@Service`) |
| Persistence | `com.mongodb.infra.adapters.entity` / `...repository` | `UserEntity`, adapters |
| Config | `com.mongodb.infra.configuration` | Beans, Mongo enablement |

**Dependency rule:** Controllers and infra depend inward on ports/domain. Do not let domain services depend on Spring Data interfaces or HTTP types.

### Known architecture leaks (do not “fix” casually)

- `User` has `@Document` / `@Id` (Mongo annotations on domain).
- Controllers bind/return `User` directly (no DTO layer).
- `UserService` is registered via `@Bean` in `BeanConfiguration`, not stereotype scanning alone.

## Critical wiring constraints

1. **`@ComponentScan` is explicit** in `SpringMongoApplication` — only:
   - `com.mongodb.infra.configuration`
   - `com.mongodb.infra.adapters.repository`
   - `com.mongodb.controller`
   - `com.mongodb.domain.adapter.service`

   New packages **will not load** unless added here (and mirrored in tests if they use their own `@ComponentScan`).

2. **New application services:** add a `@Bean` in `BeanConfiguration` (same pattern as `userService`), or annotate and ensure the package is scanned.

3. **Mongo repositories:** enabled for `com.mongodb.infra.adapters.repository` in `MongoConfig`. New Spring Data repos belong there.

4. **Field changes** require updating **both** `User` and `UserEntity` **and** `UserEntity.fromUser` / `toUser`.

5. **`UserRepository.save` discards the saved entity** — generated Mongo `_id` is not written back onto the domain `User`. After create, discover id via `getAllUsers()` / `findById` (tests already do this).

## API surface (actual)

- Context path: **`/springmongodb`** (`server.servlet.context-path`)
- Port: **8080**
- Base URL: `http://localhost:8080/springmongodb/api/users`

| Method | Path | Behavior |
|--------|------|----------|
| POST | `/api/users` | Creates user; **201** with empty string body |
| GET | `/api/users` | List users |
| GET | `/api/users/{id}` | Returns user or **null body with 200** (not 404) |
| DELETE | `/api/users/{id}` | Deletes; **200** empty |

No update/PATCH, no `@Valid`, no `@ControllerAdvice`, no pagination. `spring-boot-starter-validation` is on the classpath but unused.

### Runtime config (`application.properties`)

Uses `spring.data.mongodb.host` + `port` (localhost:27017). There is **no** `uri` / `database` in the real properties file (README claims otherwise — ignore README for this). `MongoProperties` exists but is not driving a custom `MongoClient` bean; Boot auto-config applies.

## Coding conventions

- Constructor injection; Lombok `@Data` / `@Builder` / `@RequiredArgsConstructor` as already used.
- Ports named `*Port`; Spring Data interface `IUserRepository`; adapter `UserRepository`.
- Missing entity → `null` (`orElse(null)`), not custom exceptions.
- Write operations often `void` (`createUser`, `save`, `deleteUser`).
- Collection name: `"users"`.

## Testing

- Primary test: `UserServiceTest` — **Testcontainers** integration (`mongo:6.0.2` via `TestConfig`), not mocked unit tests.
- Docker required for `mvn test`.
- Pattern: `@SpringBootTest(classes = SpringMongoApplication.class)` + `@Import(TestConfig.class)` + `@DynamicPropertySource` → `TestConfig.getMongoUri()`.
- Autowires concrete `UserService` / `UserRepository`; `@BeforeEach` calls `deleteAll()`.
- `MongoTestConfig` is **unused dead code** — do not extend it; prefer `TestConfig`.
- Surefire includes `**/*Test.java` only.
- Controllers are **not** covered by tests today.

## Common change checklists

### New endpoint / use case
1. `UserServicePort` + `UserService`
2. `UserRepositoryPort` + `UserRepository` (+ `IUserRepository` if needed)
3. `UserController` mapping
4. Extend `UserServiceTest` (create → assert via reads)

### New field on User
1. `User` + `UserEntity` + mappers
2. Update test builders/assertions
3. No migration tooling — Mongo is schemaless; be careful with primitive `int` defaults vs wrappers

### New package / bean
1. Update `SpringMongoApplication` `@ComponentScan`
2. Register service beans in `BeanConfiguration` if not stereotype-scanned
3. Update test scan/import if needed

## Commands

```bash
mvn clean install
mvn spring-boot:run
mvn test
```

Local Mongo expected at `localhost:27017` for the running app; tests spin their own container.

## Do / don't

**Do**
- Keep port interfaces as the boundary for domain logic.
- Map through `UserEntity.fromUser` / `toUser`.
- Match existing response status semantics unless the task is to change them.
- Prefer Testcontainers-style service tests consistent with `UserServiceTest`.

**Don't**
- Assume README Mongo URI/`database` properties or “unit tests with mocks” exist.
- Add packages without updating `@ComponentScan`.
- Call `IUserRepository` from controllers or `UserService`.
- Rely on `save` returning a populated domain id without changing that contract.
- Use `MongoTestConfig` as the test baseline.

## Out of scope today

Auth, DTOs, OpenAPI, update API, proper 404/validation error handling, Flyway-style migrations, frontend.
