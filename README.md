# Spring MongoDB Project

This project is a Spring Boot application that demonstrates the implementation of a RESTful API using MongoDB as the database, following hexagonal architecture principles.

## Project Structure

The project follows a hexagonal architecture (also known as Ports and Adapters) with the following structure:

```
src/
├── main/
│   ├── java/
│   │   └── com/mongodb/
│   │       ├── domain/
│   │       │   ├── User.java
│   │       │   └── ports/
│   │       │       ├── repository/
│   │       │       └── service/
│   │       ├── infra/
│   │       │   └── adapters/
│   │       │       ├── entity/
│   │       │       └── repository/
│   │       └── controller/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/mongodb/
            └── domain/
                └── adapter/
                    └── service/
                        └── UserServiceTest.java
```

## Technologies

- Java 17
- Spring Boot
- Spring Data MongoDB
- MongoDB
- Lombok
- TestContainers
- JUnit 5

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (for running MongoDB container in tests)
- MongoDB (for running the application)

## Setup and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd spring_mongodb
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## API Endpoints

### User Management

- **Create User**
  - POST `/api/users`
  - Request Body:
    ```json
    {
      "document": "123456789",
      "name": "John Doe",
      "age": 30
    }
    ```

- **Get All Users**
  - GET `/api/users`

- **Get User by ID**
  - GET `/api/users/{id}`

- **Delete User**
  - DELETE `/api/users/{id}`

## Testing

The project uses TestContainers for integration testing with MongoDB. Tests can be run using:

```bash
mvn test
```

### Test Coverage

The test suite includes:
- Unit tests for the service layer
- Integration tests using TestContainers
- CRUD operation testing
- Error handling scenarios

## Configuration

### Application Properties

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/mydatabase
spring.data.mongodb.database=mydatabase
```

### Test Properties

Tests use TestContainers to spin up a MongoDB container automatically, so no additional configuration is needed for testing.

## Architecture

This project follows hexagonal architecture with:

1. **Domain Layer**
   - Core business logic
   - Domain entities
   - Port definitions (interfaces)

2. **Infrastructure Layer**
   - MongoDB adapters
   - Repository implementations
   - Entity mappings

3. **Application Layer**
   - REST controllers
   - Service implementations
   - Configuration

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 