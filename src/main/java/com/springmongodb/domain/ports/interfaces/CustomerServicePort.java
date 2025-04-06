package com.springmongodb.domain.ports.interfaces;

import com.springmongodb.entity.Customer;

import java.util.List;

public interface CustomerServicePort {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
}
