/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: CustomerService
 * Description: Service layer providing transactional operations for customers.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        if (!customer.getEmails().isEmpty()) {
            String email = customer.getEmails().get(0).getEmail();
            if (repository.existsByEmailsEmail(email)) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        return repository.save(customer);
    }

    public Customer get(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Customer> list() {
        return repository.findAll();
    }

    public Customer update(UUID id, Customer input) {
        Customer existing = get(id);
        existing.setFirstName(input.getFirstName());
        existing.setMiddleName(input.getMiddleName());
        existing.setLastName(input.getLastName());
        existing.setAddress(input.getAddress());
        existing.setPrivacySettings(input.getPrivacySettings());
        existing.getEmails().clear();
        existing.getEmails().addAll(input.getEmails());
        existing.getPhoneNumbers().clear();
        existing.getPhoneNumbers().addAll(input.getPhoneNumbers());
        return repository.save(existing);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        repository.deleteById(id);
    }
}
