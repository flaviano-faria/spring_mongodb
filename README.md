# Spring MongoDB Project

A Spring Boot application demonstrating MongoDB integration with hexagonal architecture.

## Project Overview

This project implements a user management system using MongoDB as the data store, following hexagonal architecture principles. It demonstrates the integration of Spring Boot with MongoDB, including test containers for testing.

## Architecture

The project follows hexagonal architecture principles with the following layers:

```
com.mongodb/
├── app/                    # Application entry point
├── controller/            # REST controllers
├── domain/               # Domain layer
│   ├── adapter/         # Adapters for domain services
│   ├── ports/           # Ports (interfaces)
│   └── User.java        # Domain model
├── exception/           # Custom exceptions
└── infra/              # Infrastructure layer
    ├── adapters/       # Adapters for external services
    │   ├── entity/     # MongoDB entities
    │   └── repository/ # Repository implementations
    └── configuration/  # Configuration classes
```

## Technologies Used

- Java 17
- Spring Boot 3.2.0
- MongoDB
- TestContainers
- JUnit 5
- Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (for running tests with TestContainers)
- MongoDB server (for local development)

## Getting Started

### Building the Project

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring:boot run
```

The application will start on port 8080 by default.

### Running Tests

```bash
mvn test
```

## API Endpoints

### User Management

- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `DELETE /api/users/{id}` - Delete user by ID

## Testing

The project uses TestContainers for integration testing with MongoDB. Tests are configured to:
- Start a MongoDB container for each test class
- Clean up data between tests
- Use dynamic port allocation

### Test Structure

```java
@SpringBootTest(classes = SpringMongoApplication.class)
@Testcontainers
public class UserServiceTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.2");
    
    // Test methods
}
```

## Configuration

### MongoDB Configuration

The application uses the following MongoDB properties (configurable in `application.properties`):

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/mydatabase
```

### Test Configuration

For tests, MongoDB connection properties are dynamically configured using TestContainers:

```java
@DynamicPropertySource
static void mongoProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
}
```

## Dependencies

Key dependencies include:

- `spring-boot-starter-data-mongodb`: MongoDB integration
- `spring-boot-starter-web`: Web application support
- `testcontainers`: Container-based testing
- `junit-jupiter`: Testing framework
- `lombok`: Reducing boilerplate code

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 