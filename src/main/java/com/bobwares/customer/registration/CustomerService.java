/**
 * App: Customer Registration Package: com.bobwares.customer.registration File: CustomerService.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com) Date: 2025-10-29T19:49:40Z
 * Exports: CustomerService Description: Encapsulates transactional CRUD operations for customers,
 * including email uniqueness enforcement and DTO mapping.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\\\d{1,14}$");

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    validateEmails(request.getEmails(), null);
    Customer customer = new Customer();
    applyRequest(customer, request.getFirstName(), request.getMiddleName(), request.getLastName());
    applyAddress(customer, request.getAddress());
    applyPrivacy(customer, request.getPrivacySettings());
    customer.setEmails(mapEmails(request.getEmails()));
    customer.setPhoneNumbers(mapPhoneNumbers(request.getPhoneNumbers()));
    return toResponse(repository.save(customer));
  }

  @Transactional
  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    Customer customer = repository.findById(id).orElseThrow(() -> notFound(id));
    validateEmails(request.getEmails(), id);
    applyRequest(customer, request.getFirstName(), request.getMiddleName(), request.getLastName());
    applyAddress(customer, request.getAddress());
    applyPrivacy(customer, request.getPrivacySettings());
    customer.setEmails(mapEmails(request.getEmails()));
    customer.setPhoneNumbers(mapPhoneNumbers(request.getPhoneNumbers()));
    return toResponse(customer);
  }

  @Transactional
  public CustomerDto.Response get(UUID id) {
    return repository.findById(id).map(this::toResponse).orElseThrow(() -> notFound(id));
  }

  @Transactional
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional
  public void delete(UUID id) {
    Customer customer = repository.findById(id).orElseThrow(() -> notFound(id));
    repository.delete(customer);
  }

  private void validateEmails(List<String> emails, UUID existingId) {
    if (emails == null || emails.isEmpty()) {
      throw new IllegalArgumentException("At least one email address must be supplied.");
    }

    Set<String> normalized = new LinkedHashSet<>();
    for (String email : emails) {
      String key = email == null ? null : email.trim().toLowerCase();
      if (key == null || key.isEmpty()) {
        throw new IllegalArgumentException("Email values cannot be blank.");
      }
      if (!normalized.add(key)) {
        throw new IllegalArgumentException("Duplicate email detected: " + email);
      }
      if (!EMAIL_PATTERN.matcher(email).matches()) {
        throw new IllegalArgumentException("Invalid email address: " + email);
      }
      if (email.length() > 255) {
        throw new IllegalArgumentException("Email exceeds allowed length: " + email);
      }
      if (existingId == null) {
        if (repository.existsByEmailsEmailIgnoreCase(email)) {
          throw new IllegalArgumentException("Email already registered: " + email);
        }
      } else if (repository.existsByIdNotAndEmailsEmailIgnoreCase(existingId, email)) {
        throw new IllegalArgumentException("Email already registered: " + email);
      }
    }
  }

  private void applyRequest(
      Customer customer, String firstName, String middleName, String lastName) {
    customer.setFirstName(firstName);
    customer.setMiddleName(middleName);
    customer.setLastName(lastName);
  }

  private void applyAddress(Customer customer, CustomerDto.Address address) {
    if (address == null) {
      customer.setAddressLine1(null);
      customer.setAddressLine2(null);
      customer.setAddressCity(null);
      customer.setAddressState(null);
      customer.setAddressPostalCode(null);
      customer.setAddressCountry(null);
      return;
    }
    customer.setAddressLine1(address.getLine1());
    customer.setAddressLine2(address.getLine2());
    customer.setAddressCity(address.getCity());
    customer.setAddressState(address.getState());
    customer.setAddressPostalCode(address.getPostalCode());
    customer.setAddressCountry(address.getCountry());
  }

  private void applyPrivacy(Customer customer, CustomerDto.PrivacySettings settings) {
    if (settings == null) {
      throw new IllegalArgumentException("Privacy settings must be provided.");
    }
    customer.setMarketingEmailsEnabled(settings.getMarketingEmailsEnabled());
    customer.setTwoFactorEnabled(settings.getTwoFactorEnabled());
  }

  private List<CustomerEmail> mapEmails(List<String> emails) {
    if (emails == null) {
      return List.of();
    }
    return emails.stream()
        .map(
            value -> {
              CustomerEmail email = new CustomerEmail();
              email.setEmail(value);
              return email;
            })
        .collect(Collectors.toList());
  }

  private List<CustomerPhoneNumber> mapPhoneNumbers(List<CustomerDto.PhoneNumber> phoneNumbers) {
    if (phoneNumbers == null || phoneNumbers.isEmpty()) {
      return List.of();
    }
    return phoneNumbers.stream()
        .map(
            source -> {
              String number = source.getNumber();
              if (number != null && !PHONE_PATTERN.matcher(number).matches()) {
                throw new IllegalArgumentException("Invalid phone number: " + number);
              }
              CustomerPhoneNumber target = new CustomerPhoneNumber();
              target.setContactType(source.getType());
              target.setPhoneNumber(number);
              return target;
            })
        .collect(Collectors.toList());
  }

  private CustomerDto.Response toResponse(Customer customer) {
    return CustomerDto.Response.builder()
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .middleName(customer.getMiddleName())
        .lastName(customer.getLastName())
        .address(
            CustomerDto.Address.builder()
                .line1(customer.getAddressLine1())
                .line2(customer.getAddressLine2())
                .city(customer.getAddressCity())
                .state(customer.getAddressState())
                .postalCode(customer.getAddressPostalCode())
                .country(customer.getAddressCountry())
                .build())
        .emails(customer.getEmails().stream().map(CustomerEmail::getEmail).toList())
        .phoneNumbers(
            customer.getPhoneNumbers().stream()
                .map(
                    number ->
                        CustomerDto.PhoneNumber.builder()
                            .type(number.getContactType())
                            .number(number.getPhoneNumber())
                            .build())
                .toList())
        .privacySettings(
            CustomerDto.PrivacySettings.builder()
                .marketingEmailsEnabled(customer.getMarketingEmailsEnabled())
                .twoFactorEnabled(customer.getTwoFactorEnabled())
                .build())
        .build();
  }

  private EntityNotFoundException notFound(UUID id) {
    return new EntityNotFoundException("Customer not found: " + id);
  }
}
