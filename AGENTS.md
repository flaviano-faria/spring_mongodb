# AGENTS.md — spring_mongodb

Guidance for AI agents working in this repository. `README.md` is the human-facing overview; prefer this file if they ever diverge.

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

Important: Before editing a layer, read its file under `harness/layers/`. The map is `harness/README.md`.

### Known architecture leaks (do not “fix” casually)

- `User` and `UserEntity` both have `@Document` / `@Id`. The annotations on `User` are a leak. Leave both in place unless the task is to remove them from the domain.
- Controllers bind/return `User` directly (no DTO layer).
- `UserService` is registered via `@Bean` in `BeanConfiguration`, not stereotype scanning alone.

## Critical wiring constraints

1. **`@ComponentScan` is explicit** in `SpringMongoApplication` — only:
   - `com.mongodb.infra.configuration`
   - `com.mongodb.infra.adapters.repository`
   - `com.mongodb.controller`
   - `com.mongodb.domain.adapter.service`

   A new package is invisible until it is added to this list. `UserServiceTest` repeats the same four packages on its own `@ComponentScan`, and Spring does not process that annotation. The test context is `@SpringBootTest(classes = SpringMongoApplication.class)` plus `@Import(TestConfig.class)`. `MongoTestConfig` declares a third `@ComponentScan` and omits `com.mongodb.controller`. That class is unused. Do not import it, extend it, or edit its list to match.

2. **New application services:** add a `@Bean` in `BeanConfiguration` (same pattern as `userService`), or put `@Component` / `@Service` on a class whose package is already on `SpringMongoApplication`'s `@ComponentScan`. `UserService` has no stereotype. The package `com.mongodb.domain.adapter.service` is scanned, and that scan still does not register `UserService`.

3. **Mongo repositories:** enabled for `com.mongodb.infra.adapters.repository` in `MongoConfig`. New Spring Data repos belong there.

4. **Field changes** require updating **both** `User` and `UserEntity` **and** `UserEntity.fromUser` / `toUser`.

5. **`UserRepository.save` discards the saved entity** — generated Mongo `_id` is not written back onto the domain `User`. After create, discover id via `getAllUsers()` / `findById` (tests already do this).

## API surface (actual)

- Context path: **`/springmongodb`** (`server.servlet.context-path`)
- Port: **8080**
- Base URL: `http://localhost:8080/springmongodb/api/users`
- Controller mappings are relative to the context path. `POST /api/users` in `UserController` is `POST http://localhost:8080/springmongodb/api/users`.

| Method | Path | Behavior |
|--------|------|----------|
| POST | `/api/users` | Creates user; **201** with empty string body |
| GET | `/api/users` | List users |
| GET | `/api/users/{id}` | Returns user or **null body with 200** (not 404) |
| DELETE | `/api/users/{id}` | Deletes; **200** empty |

No update/PATCH, no `@Valid`, no `@ControllerAdvice`, no pagination. `spring-boot-starter-validation` is on the classpath but unused.

### Runtime config (`application.properties`)

`src/main/resources/application.properties` sets `spring.data.mongodb.host` and `port` (localhost:27017) and `server.servlet.context-path=/springmongodb`. There is **no** `uri` / `database` in that file, so Boot's default database `test` is used. `MongoProperties` declares `uri`, `database`, `host`, `port`, `username`, `password`, and `authenticationDatabase`, and it does not build a `MongoClient`. Setting those fields does not change the connection. Boot auto-config applies.

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
- `MongoTestConfig` is **unused dead code**. Its `@ComponentScan` omits `com.mongodb.controller` and is not loaded. Do not import it, extend it, or update that list. Prefer `TestConfig`.
- Surefire includes `**/*Test.java` only.
- Controllers are **not** covered by tests today.
- Mockito and MockMvc are available through `spring-boot-starter-test`; no mocked unit tests exist yet.

## Unit test policy (mandatory)

**Every change to a class under `src/main/java` must update its related test in the same change.** If the class has no test yet, create one. A change is not done until the related tests are updated and pass.

### Rules

1. **Location:** mirror the production package under `src/test/java`, named `<ClassName>Test.java` (e.g. `com.mongodb.domain.adapter.service.UserService` → `src/test/java/com/mongodb/domain/adapter/service/UserServiceTest.java`). The `Test` suffix is required or Surefire skips the file.
2. **What to update:**
   - New public method → add test methods for the success path and the edge cases (e.g. missing id, empty list).
   - Changed behavior → update assertions to the new behavior.
   - Renamed/removed method → rename/remove its tests.
   - New or changed field on `User` / `UserEntity` → update builders and assertions in every test that builds or reads them.
   - Interface (port) change → update the tests of every implementation.
3. **Never** make tests pass by deleting or weakening assertions, adding `@Disabled`, or running with `-DskipTests`.
4. **Style:** follow `UserServiceTest` — JUnit 5 `Assertions`, `<methodName>Test` names, `// Setup` / `// Execute` / `// Assert` sections, clean state in `@BeforeEach`.
5. **Verify before finishing:** run the related test, then the full suite, and report the result. If Docker is unavailable, say the Testcontainers tests could not run; do not claim they passed.

   ```powershell
   .\mvnw.cmd test -Dtest=UserServiceTest
   .\mvnw.cmd test
   ```

6. **Test hook:** `.cursor/hooks.json` enforces this. When the agent edits `src/main`, `src/test`, or `pom.xml`, the `stop` hook runs `.\mvnw.cmd test` at the end of the turn and sends failures (or "Docker not running") back as a follow-up message, up to 3 times. Fix the reported failures under the rules above. Log: `.cursor/hooks/.state/last-test-run.log`.

### Class → test map

| Production class | Test | Style |
|------------------|------|-------|
| `UserService` | `UserServiceTest` (exists) | Testcontainers integration; Mockito unit tests against a mocked `UserRepositoryPort` may be added alongside |
| `UserController` | `UserControllerTest` (create on first change) | `MockMvcBuilders.standaloneSetup(new UserController(mockPort))` with a Mockito mock of `UserServicePort`; include the status codes and bodies from the API table |
| `UserRepository` / `IUserRepository` | `UserRepositoryTest` (create on first change) | Testcontainers, same setup as `UserServiceTest` |
| `UserEntity` / `User` | `UserEntityTest` (create on first change) | Plain JUnit round-trip of `fromUser` / `toUser`, checking every field |
| `UserServicePort` / `UserRepositoryPort` | Tests of their implementations | — |
| `BeanConfiguration`, `MongoConfig`, `MongoProperties`, `SpringMongoApplication` | `UserServiceTest` (loads the full Spring context) | Must still pass; add a dedicated test if the class gains logic |

Prefer `standaloneSetup` over `@WebMvcTest` for controllers: the explicit `@ComponentScan` on `SpringMongoApplication` would pull repository beans into a web slice and require MongoDB.

## Common change checklists

### New endpoint / use case
1. `UserServicePort` + `UserService`
2. `UserRepositoryPort` + `UserRepository` (+ `IUserRepository` if needed)
3. `UserController` mapping
4. Extend `UserServiceTest` (create → assert via reads) and add/update `UserControllerTest` for the new mapping
5. Update tests for every other class touched (see the unit test policy)

### New field on User
1. `User` + `UserEntity` + mappers
2. Update test builders/assertions in `UserServiceTest` and add/update `UserEntityTest` for the mapping
3. No migration tooling — Mongo is schemaless; be careful with primitive `int` defaults vs wrappers

### New package / bean
1. Update `@ComponentScan` on `SpringMongoApplication` only. The copy on `UserServiceTest` is not loaded
2. Register a service with a `@Bean` in `BeanConfiguration`, or with a stereotype when its package is already on that scan
3. Update test imports if the test context needs the new type
4. Create `<ClassName>Test.java` for each new class in the mirrored test package

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
- Update or create the related test whenever a production class changes, and run it before finishing.

**Don't**
- Assume Mongo `uri` / `database` properties or mocked unit tests already exist.
- Finish a class change without its test change, or skip/disable tests to get a green build.
- Add a package without updating `@ComponentScan` on `SpringMongoApplication`.
- Import `MongoTestConfig`, or edit its `@ComponentScan` so it matches the application.
- Call `IUserRepository` from controllers or `UserService`.
- Rely on `save` returning a populated domain id without changing that contract.
- Use `MongoTestConfig` as the test baseline.

## Out of scope today

Auth, DTOs, OpenAPI, update API, proper 404/validation error handling, Flyway-style migrations, frontend.
