/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerService
 * Description: Transactional service orchestrating CRUD operations and uniqueness validation for customers.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerMapper;
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

  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    List<String> emails = CustomerMapper.normalizeEmails(request.getEmails());
    CustomerMapper.ensureUniqueEmails(emails);
    ensureEmailsAvailable(emails, null);
    Customer saved = repository.save(CustomerMapper.toEntity(request, emails));
    return CustomerMapper.toResponse(saved);
  }

  @Transactional
  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    if (!id.equals(request.getId())) {
      throw new IllegalArgumentException("Path id does not match request id");
    }
    Customer existing = repository.findById(id).orElseThrow(() -> notFound(id));
    List<String> emails = CustomerMapper.normalizeEmails(request.getEmails());
    CustomerMapper.ensureUniqueEmails(emails);
    ensureEmailsAvailable(emails, id);
    CustomerMapper.apply(request, existing, emails);
    Customer saved = repository.save(existing);
    return CustomerMapper.toResponse(saved);
  }

  @Transactional
  public void delete(UUID id) {
    Customer existing = repository.findById(id).orElseThrow(() -> notFound(id));
    repository.delete(existing);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public CustomerDto.Response get(UUID id) {
    return repository.findById(id).map(CustomerMapper::toResponse).orElseThrow(() -> notFound(id));
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(CustomerMapper::toResponse).toList();
  }

  private void ensureEmailsAvailable(List<String> emails, UUID excludedId) {
    for (String email : emails) {
      if (excludedId == null) {
        if (repository.existsByEmail(email)) {
          throw new IllegalArgumentException("Email address already registered: " + email);
        }
      } else if (repository.existsByEmailExcludingId(email, excludedId)) {
        throw new IllegalArgumentException("Email address already registered: " + email);
      }
    }
  }

  private EntityNotFoundException notFound(UUID id) {
    return new EntityNotFoundException("Customer %s not found".formatted(id));
  }
}
