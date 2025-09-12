/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: CustomerService
 * Description: Provides customer business operations.
 */
package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Encapsulates business logic for managing customers.
 */
@Service
public class CustomerService {

    private final CustomerRepository repository;

    /**
     * Creates a new service with the given repository.
     *
     * @param repository customer data repository
     */
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided customer.
     *
     * @param customer customer to save
     * @return the persisted customer
     */
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    /**
     * Retrieves a customer by identifier.
     *
     * @param id customer identifier
     * @return customer if present
     */
    public Optional<Customer> findById(UUID id) {
        return repository.findById(id);
    }
}
