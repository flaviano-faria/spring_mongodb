package com.mongodb.domain.adapter.service;

import com.mongodb.app.SpringMongoApplication;
import com.mongodb.config.TestConfig;
import com.mongodb.domain.User;
import com.mongodb.infra.adapters.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringMongoApplication.class)
@Import(TestConfig.class)
@Testcontainers
@ComponentScan(basePackages = {
        "com.mongodb.domain.adapter.service",
        "com.mongodb.infra.adapters.repository",
        "com.mongodb.infra.configuration",
        "com.mongodb.controller"
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", TestConfig::getMongoUri);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUserTest() {
        // Setup
        User user = User.builder()
                .document("123456789")
                .name("John Doe")
                .age(30)
                .build();

        // Execute
        userService.createUser(user);
        List<User> users = userService.getAllUsers();
        // Assert
        assertNotNull(users.get(0).getId());
        assertNotNull(users.get(0).getId());
        assertEquals("123456789", users.get(0).getDocument());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals(30, users.get(0).getAge());
    }

    @Test
    void getAllUsersTest() {
        // Setup
        User user1 = User.builder()
                .document("123456789")
                .name("John Doe")
                .age(30)
                .build();
        userService.createUser(user1);

        User user2 = User.builder()
                .document("987654321")
                .name("Jane Doe")
                .age(25)
                .build();
        userService.createUser(user2);

        // Execute
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("John Doe")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Jane Doe")));
    }

    @Test
    void getUserByIdTest() {
        // Setup
        User user = User.builder()
                .document("123456789")
                .name("John Doe")
                .age(30)
                .build();
        userService.createUser(user);
        List<User> users = userService.getAllUsers();
        // Execute
        User foundUser = userService.getUserById(users.get(0).getId()).orElse(null);

        // Assert
        assertNotNull(foundUser);
        assertEquals(users.get(0).getId(), foundUser.getId());
        assertEquals("123456789", foundUser.getDocument());
        assertEquals("John Doe", foundUser.getName());
        assertEquals(30, foundUser.getAge());
    }

    @Test
    void deleteUserTest() {
        // Setup
        User user = User.builder()
                .document("123456789")
                .name("John Doe")
                .age(30)
                .build();
        userService.createUser(user);
        List<User> users = userService.getAllUsers();
        // Execute
        userService.deleteUser(users.get(0).getId());

        // Assert
        assertNull(userService.getUserById(users.get(0).getId()));
    }
} 