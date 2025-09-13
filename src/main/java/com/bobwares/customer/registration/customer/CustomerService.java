/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: CustomerService
 * Description: Provides business operations for managing customers.
 */
package com.bobwares.customer.registration.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Optional<Customer> findById(UUID id) {
        return repository.findById(id);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
