/**
 * App: Customer Registration Package: com.bobwares.customer.registration.service File:
 * CustomerService.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: CustomerService Description: Implements transactional operations for managing customer
 * aggregates and enforcing uniqueness guarantees.
 */
package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    ensureEmailsAreUnique(request.getEmails(), null);
    Customer customer = CustomerDto.toEntity(request);
    Customer saved = repository.save(customer);
    return CustomerDto.fromEntity(saved);
  }

  @Transactional
  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    Customer customer = repository.findById(id).orElseThrow(() -> missingCustomer(id));
    ensureEmailsAreUnique(request.getEmails(), id);
    CustomerDto.updateEntity(customer, request);
    return CustomerDto.fromEntity(customer);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw missingCustomer(id);
    }
    repository.deleteById(id);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public CustomerDto.Response get(UUID id) {
    Customer customer = repository.findById(id).orElseThrow(() -> missingCustomer(id));
    return CustomerDto.fromEntity(customer);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(CustomerDto::fromEntity).toList();
  }

  private void ensureEmailsAreUnique(List<String> emails, UUID currentCustomerId) {
    if (emails == null) {
      return;
    }
    emails.stream()
        .distinct()
        .forEach(
            email -> {
              if (repository.existsByEmailsEmailIgnoreCase(email)
                  && (currentCustomerId == null
                      || repository
                          .findByEmailsEmailIgnoreCase(email)
                          .map(Customer::getId)
                          .filter(existingId -> !existingId.equals(currentCustomerId))
                          .isPresent())) {
                throw new IllegalArgumentException("Email already registered: " + email);
              }
            });
  }

  private EntityNotFoundException missingCustomer(UUID id) {
    return new EntityNotFoundException("Customer not found: " + id);
  }
}
