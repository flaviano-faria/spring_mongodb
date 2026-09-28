# Spring MongoDB — Hexagonal Architecture

A Spring Boot application exposing a User CRUD REST API backed by MongoDB, organized with **hexagonal architecture** (ports and adapters).

## Tech stack

| Component | Version |
|-----------|---------|
| Java | 17 |
| Spring Boot | 3.2.0 (Web, Data MongoDB, Validation) |
| Lombok | 1.18.36 |
| JUnit | 5.10.1 |
| Testcontainers | 1.19.3 (MongoDB `mongo:6.0.2`) |
| Build | Maven (wrapper included), `war` packaging |

## Architecture

```
UserController  →  UserServicePort  →  UserService  →  UserRepositoryPort  →  UserRepository  →  IUserRepository  →  MongoDB
(driving)          (inbound port)      (domain impl)   (outbound port)        (adapter)          (Spring Data)
```

The domain depends only on its ports. Controllers and infrastructure adapters depend inward on those ports, never the other way around.

| Layer | Package | Contents |
|-------|---------|----------|
| Bootstrap | `com.mongodb.app` | `SpringMongoApplication` |
| HTTP adapter | `com.mongodb.controller` | `UserController` |
| Domain model | `com.mongodb.domain` | `User` |
| Ports | `com.mongodb.domain.ports.service` / `.repository` | `UserServicePort`, `UserRepositoryPort` |
| Domain service | `com.mongodb.domain.adapter.service` | `UserService` |
| Persistence adapter | `com.mongodb.infra.adapters.entity` / `.repository` | `UserEntity`, `UserRepository`, `IUserRepository` |
| Configuration | `com.mongodb.infra.configuration` | `BeanConfiguration`, `MongoConfig`, `MongoProperties` |

`SpringMongoApplication` uses an explicit `@ComponentScan`, so new packages must be added there before Spring will load them.

## Project structure

```
spring_mongodb/
├── src/
│   ├── main/
│   │   ├── java/com/mongodb/
│   │   │   ├── app/                      # Spring Boot entry point
│   │   │   ├── controller/               # REST adapter
│   │   │   ├── domain/
│   │   │   │   ├── User.java
│   │   │   │   ├── adapter/service/      # UserService
│   │   │   │   └── ports/                # service/ and repository/ interfaces
│   │   │   └── infra/
│   │   │       ├── adapters/entity/      # UserEntity + mappers
│   │   │       ├── adapters/repository/  # Spring Data repository + adapter
│   │   │       └── configuration/        # Bean and Mongo configuration
│   │   └── resources/application.properties
│   └── test/java/com/mongodb/
│       ├── config/                       # Testcontainers configuration
│       └── domain/adapter/service/       # UserServiceTest
├── pom.xml
├── mvnw / mvnw.cmd                       # Maven wrapper
└── AGENTS.md                             # Guidance for AI coding agents
```

## Prerequisites

- JDK 17 or later, with `JAVA_HOME` pointing to it
- Docker (runs MongoDB locally and is required by the tests)
- Maven is optional; the wrapper (`mvnw` / `mvnw.cmd`) downloads it automatically

## Getting started

1. Clone the repository:

   ```bash
   git clone https://github.com/flaviano-faria/spring_mongodb.git
   cd spring_mongodb
   ```

2. Start MongoDB on `localhost:27017`:

   ```bash
   docker run --name mongodb -p 27017:27017 -d mongodb/mongodb-community-server:latest
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run        # Linux / macOS
   .\mvnw.cmd spring-boot:run    # Windows
   ```

The API is available at `http://localhost:8080/springmongodb/api/users`.

### Useful Docker commands

```bash
docker start mongodb              # restart the container after it stops
docker exec -it mongodb mongosh   # open a Mongo shell inside the container
```

## Configuration

`src/main/resources/application.properties`:

```properties
spring.application.name=spring_mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
server.port=8080
server.servlet.context-path=/springmongodb
```

No database name is configured, so Spring Boot uses its default database, `test`. Documents are stored in the `users` collection. To use another database, add `spring.data.mongodb.database=<name>`.

## API

Base URL: `http://localhost:8080/springmongodb`

| Method | Path | Description | Response |
|--------|------|-------------|----------|
| `POST` | `/api/users` | Create a user | `201 Created`, empty body |
| `GET` | `/api/users` | List all users | `200 OK`, JSON array |
| `GET` | `/api/users/{id}` | Get a user by id | `200 OK`, user JSON, or an empty body if not found |
| `DELETE` | `/api/users/{id}` | Delete a user | `200 OK`, empty body |

User payload:

```json
{
  "document": "123456789",
  "name": "John Doe",
  "age": 30
}
```

Examples:

```bash
curl -X POST http://localhost:8080/springmongodb/api/users \
  -H "Content-Type: application/json" \
  -d '{"document":"123456789","name":"John Doe","age":30}'

curl http://localhost:8080/springmongodb/api/users
curl http://localhost:8080/springmongodb/api/users/<id>
curl -X DELETE http://localhost:8080/springmongodb/api/users/<id>
```

Current behavior to be aware of:

- `POST` does not return the generated id; list users to find it.
- A missing id on `GET /api/users/{id}` returns `200` with an empty body, not `404`.
- There is no update endpoint, request validation, or pagination yet.

## Testing

Tests are integration tests that start a real MongoDB (`mongo:6.0.2`) with Testcontainers, so **Docker must be running**. No local MongoDB or extra configuration is needed.

```bash
./mvnw test                          # full suite
./mvnw test -Dtest=UserServiceTest   # single test class
```

Surefire only picks up classes named `*Test.java`.

## Building

```bash
./mvnw clean package
```

This produces `target/spring_mongodb-0.0.1-SNAPSHOT.war`, which can be deployed to a servlet container or run with `java -jar`.

## AI-assisted development

- [`AGENTS.md`](AGENTS.md) documents architecture constraints, conventions, and the unit test policy for AI coding agents.
- `.cursor/hooks.json` runs the test suite automatically at the end of any Cursor agent turn that changed code, and sends failures back to the agent.

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Make your changes and add or update the related tests.
4. Make sure `./mvnw test` passes.
5. Commit and push: `git commit -m "Add my feature"` and `git push origin feature/my-feature`
6. Open a pull request.

For questions or bugs, open an issue in the [GitHub repository](https://github.com/flaviano-faria/spring_mongodb/issues).
