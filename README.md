# Spring MongoDB Application

This is a Spring Boot application that demonstrates the integration between Spring Framework and MongoDB, providing a robust foundation for building scalable and efficient web applications with NoSQL database support.

## Technologies Used

- Java 17
- Spring Boot 3.2.5
- Spring Data MongoDB
- Spring Web
- Maven
- MongoDB
- Apache Tomcat (Embedded)
- Testcontainers (for integration testing)

## Prerequisites

Before running this application, make sure you have the following installed:

- Java Development Kit (JDK) 17 or later
- MongoDB (Make sure MongoDB server is running on your machine)
- Maven 3.x
- Docker (required for running test containers)
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Project Structure

The project follows standard Maven project structure:

```
spring_mongodb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/springmongodb/
│   │   │       ├── exec/           # Main application class
│   │   │       ├── entity/         # Domain models
│   │   │       └── repository/     # MongoDB repositories
│   │   └── resources/             # Application properties and static resources
│   └── test/
│       └── java/
│           └── com/springmongodb/  # Test classes
├── pom.xml                        # Maven configuration file
├── .gitignore
└── README.md
```

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/spring_mongodb.git
   cd spring_mongodb
   ```

2. Configure MongoDB:
   - Make sure MongoDB is running on your machine
   - Update `application.properties` with your MongoDB configuration if needed

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Features

- Spring Boot 3.2.5 framework
- MongoDB integration using Spring Data MongoDB
- RESTful API endpoints
- War packaging support
- Integration testing with Testcontainers
- Custom MongoDB repository queries

## Testing

The project uses Testcontainers for integration testing, providing isolated MongoDB instances for each test run.

### Running Tests

```bash
mvn test
```

### Test Features

- Automated MongoDB container management
- Isolated test environment
- Integration testing with real MongoDB instance
- Example test cases for Customer entity operations

### Test Container Configuration

The test configuration uses:
- MongoDB 4.4.2 container
- Dynamic property configuration for MongoDB URI
- Spring Boot test context with proper configuration

## Configuration

The application can be configured through the `application.properties` file located in `src/main/resources/`. Key configuration properties include:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=your_database_name
```

## Building for Production

To build a WAR file for deployment:

```bash
mvn clean package
```

The WAR file will be generated in the `target` directory.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

For support and questions, please open an issue in the GitHub repository. 