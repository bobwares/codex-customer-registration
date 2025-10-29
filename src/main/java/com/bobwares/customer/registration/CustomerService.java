/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerService
 * Description: Encapsulates transactional customer persistence operations and enforces email uniqueness invariants.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  public Customer create(CustomerDto.CreateRequest request) {
    Customer customer = toEntity(request);
    ensureEmailUniqueness(customer.getEmails(), null);
    ensurePrimaryEmail(customer);
    return repository.save(customer);
  }

  public Customer update(UUID id, CustomerDto.UpdateRequest request) {
    Customer existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

    Customer incoming = toEntity(request);
    ensureEmailUniqueness(incoming.getEmails(), id);
    merge(existing, incoming);
    ensurePrimaryEmail(existing);
    return repository.save(existing);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public Customer get(UUID id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public List<Customer> list() {
    return repository.findAll();
  }

  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Customer not found");
    }
    repository.deleteById(id);
  }

  private Customer toEntity(CustomerDto.CreateRequest request) {
    Customer customer = new Customer();
    customer.setFirstName(request.firstName());
    customer.setMiddleName(request.middleName());
    customer.setLastName(request.lastName());
    customer.setEmails(new LinkedHashSet<>(request.emails()));
    if (request.phoneNumbers() != null) {
      customer.setPhoneNumbers(new LinkedHashSet<>(request.phoneNumbers().stream().map(this::toPhoneNumber).toList()));
    }
    customer.setAddress(toAddress(request.address()));
    customer.setPrivacySettings(toPrivacy(request.privacySettings()));
    return customer;
  }

  private Customer toEntity(CustomerDto.UpdateRequest request) {
    CustomerDto.CreateRequest createRequest =
        new CustomerDto.CreateRequest(
            request.firstName(),
            request.middleName(),
            request.lastName(),
            request.emails(),
            request.address(),
            request.privacySettings(),
            request.phoneNumbers());
    return toEntity(createRequest);
  }

  private void merge(Customer target, Customer source) {
    target.setFirstName(source.getFirstName());
    target.setMiddleName(source.getMiddleName());
    target.setLastName(source.getLastName());
    target.setEmails(new LinkedHashSet<>(source.getEmails()));
    target.setPhoneNumbers(new LinkedHashSet<>(source.getPhoneNumbers()));
    target.setAddress(source.getAddress());
    target.setPrivacySettings(source.getPrivacySettings());
  }

  private void ensureEmailUniqueness(Set<String> emails, UUID excludeId) {
    List<String> duplicates = new ArrayList<>();
    for (String email : emails) {
      repository
          .findByEmail(email)
          .filter(customer -> excludeId == null || !customer.getId().equals(excludeId))
          .ifPresent(customer -> duplicates.add(email));
    }
    if (!duplicates.isEmpty()) {
      throw new IllegalArgumentException("Email address already in use: " + duplicates.get(0));
    }
  }

  private void ensurePrimaryEmail(Customer customer) {
    if (customer.getEmails().isEmpty()) {
      throw new IllegalArgumentException("At least one email address is required");
    }
    String primary = customer.getEmails().iterator().next();
    customer.setPrimaryEmail(primary);
  }

  private PhoneNumber toPhoneNumber(CustomerDto.PhoneNumberRequest request) {
    PhoneNumber number = new PhoneNumber();
    number.setType(request.type());
    number.setNumber(request.number());
    return number;
  }

  private PostalAddress toAddress(CustomerDto.PostalAddressRequest request) {
    PostalAddress address = new PostalAddress();
    address.setLine1(request.line1());
    address.setLine2(request.line2());
    address.setCity(request.city());
    address.setState(request.state());
    address.setPostalCode(request.postalCode());
    address.setCountry(request.country().toUpperCase());
    return address;
  }

  private PrivacySettings toPrivacy(CustomerDto.PrivacySettingsRequest request) {
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(request.marketingEmailsEnabled());
    settings.setTwoFactorEnabled(request.twoFactorEnabled());
    return settings;
  }
}
