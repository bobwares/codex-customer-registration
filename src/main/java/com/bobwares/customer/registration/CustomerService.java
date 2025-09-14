/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: CustomerService
 * Description: Service layer providing transactional CRUD operations for Customer entities.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        customer.getEmails().forEach(e -> {
            if (repository.existsByEmailsEmail(e.getEmail())) {
                throw new IllegalArgumentException("Email already used: " + e.getEmail());
            }
            e.setCustomer(customer);
        });
        customer.getPhoneNumbers().forEach(p -> p.setCustomer(customer));
        return repository.save(customer);
    }

    @Transactional
    public Customer update(UUID id, Customer update) {
        Customer existing = get(id);
        existing.setFirstName(update.getFirstName());
        existing.setMiddleName(update.getMiddleName());
        existing.setLastName(update.getLastName());
        existing.setAddress(update.getAddress());
        existing.setPrivacySettings(update.getPrivacySettings());

        existing.getEmails().clear();
        update.getEmails().forEach(e -> {
            if (repository.existsByEmailsEmail(e.getEmail()) && existing.getEmails().stream().noneMatch(x -> x.getEmail().equals(e.getEmail()))) {
                throw new IllegalArgumentException("Email already used: " + e.getEmail());
            }
            e.setCustomer(existing);
            existing.getEmails().add(e);
        });

        existing.getPhoneNumbers().clear();
        update.getPhoneNumbers().forEach(p -> {
            p.setCustomer(existing);
            existing.getPhoneNumbers().add(p);
        });

        return repository.save(existing);
    }

    @Transactional
    public Customer get(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public List<Customer> list() {
        return repository.findAll();
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
