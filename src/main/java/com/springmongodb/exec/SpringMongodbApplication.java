package com.springmongodb.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.springmongodb.entity.Customer;
import com.springmongodb.entity.User;
import com.springmongodb.repository.CustomerRepository;
import com.springmongodb.repository.UserRepository;


@ComponentScan(basePackages = {"com.springmongodb.repository"})
@EnableMongoRepositories ("com.springmongodb.repository") // this fix the problem
@SpringBootApplication
public class SpringMongodbApplication implements CommandLineRunner{

	 @Autowired
	 private CustomerRepository repository;
	 
	 @Autowired
	 private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringMongodbApplication.class, args);
	}
	
	
	@Override
	  public void run(String... args) throws Exception {

	    repository.deleteAll();

	    // save a couple of customers
	    repository.save(new Customer("Alice", "Smith"));
	    repository.save(new Customer("Bob", "Smith"));

	    // fetch all customers
	    System.out.println("Customers found with findAll():");
	    System.out.println("-------------------------------");
	    for (Customer customer : repository.findAll()) {
	      System.out.println(customer);
	    }
	    System.out.println();

	    // fetch an individual customer
	    System.out.println("Customer found with findByFirstName('Alice'):");
	    System.out.println("--------------------------------");
	    System.out.println(repository.findByFirstName("Alice"));

	    System.out.println("Customers found with findByLastName('Smith'):");
	    System.out.println("--------------------------------");
	    for (Customer customer : repository.findByLastName("Smith")) {
	      System.out.println(customer);
	    }
	    
	    /*############################  User ####################    */

	    for(User user : userRepository.findAll()) {
	    	System.out.println(user);
	    }
	  }


}
