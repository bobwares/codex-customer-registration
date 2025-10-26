/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.service
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerService
 * Description: Domain service orchestrating CRUD operations and uniqueness validation for customers.
 */
package com.bobwares.customer.registration.customer.service;

import com.bobwares.customer.registration.customer.model.Customer;
import com.bobwares.customer.registration.customer.model.CustomerEmail;
import com.bobwares.customer.registration.customer.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        ensureUniqueEmails(customer, null);
        return repository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer get(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Customer %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<Customer> list() {
        return repository.findAll();
    }

    public Customer update(UUID id, Customer changes) {
        Customer existing = get(id);

        existing.setFirstName(changes.getFirstName());
        existing.setMiddleName(changes.getMiddleName());
        existing.setLastName(changes.getLastName());
        existing.setAddress(changes.getAddress());
        existing.setPrivacySettings(changes.getPrivacySettings());

        existing.clearEmails();
        for (CustomerEmail email : changes.getEmails()) {
            CustomerEmail clone = new CustomerEmail();
            clone.setEmail(email.getEmail());
            existing.addEmail(clone);
        }

        existing.clearPhoneNumbers();
        for (CustomerPhoneNumber number : changes.getPhoneNumbers()) {
            CustomerPhoneNumber clone = new CustomerPhoneNumber();
            clone.setPhoneNumber(number.getPhoneNumber());
            clone.setPhoneType(number.getPhoneType());
            existing.addPhoneNumber(clone);
        }

        ensureUniqueEmails(existing, id);
        return repository.save(existing);
    }

    public void delete(UUID id) {
        Customer existing = get(id);
        repository.delete(existing);
    }

    private void ensureUniqueEmails(Customer customer, UUID excludeId) {
        for (CustomerEmail email : customer.getEmails()) {
            boolean exists = excludeId == null
                    ? repository.existsByEmailIgnoreCase(email.getEmail())
                    : repository.existsByEmailIgnoreCaseForOtherCustomer(email.getEmail(), excludeId);
            if (exists) {
                throw new IllegalArgumentException("Email %s already exists".formatted(email.getEmail()));
            }
        }
    }
}
