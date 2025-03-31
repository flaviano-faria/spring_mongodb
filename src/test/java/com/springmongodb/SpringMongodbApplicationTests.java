package com.springmongodb;

import com.springmongodb.entity.Customer;
import com.springmongodb.repository.CustomerRepository;
import com.springmongodb.exec.SpringMongodbApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SpringMongodbApplication.class)
@Testcontainers
class SpringMongodbApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));

	@DynamicPropertySource
	static void mongoProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldSaveAndRetrieveCustomer() {
		// Given
		Customer customer = new Customer("John", "Doe");

		// When
		Customer savedCustomer = customerRepository.save(customer);
		Customer retrievedCustomer = customerRepository.findByFirstName("John");

		// Then
		assertThat(retrievedCustomer).isNotNull();
		assertThat(retrievedCustomer.firstName).isEqualTo("John");
		assertThat(retrievedCustomer.lastName).isEqualTo("Doe");
	}
} 