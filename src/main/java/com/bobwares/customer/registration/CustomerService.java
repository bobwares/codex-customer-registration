/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerService
 * Description: Encapsulates transactional customer CRUD workflows including mapping to DTOs and uniqueness enforcement.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerDto.AddressDto;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumberDto;
import com.bobwares.customer.registration.api.CustomerDto.PrivacySettingsDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.CustomerEmail;
import com.bobwares.customer.registration.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    Objects.requireNonNull(request, "request");
    validateUniqueEmails(request.emails());

    Customer customer = new Customer();
    applyFields(customer, request.firstName(), request.middleName(), request.lastName());
    customer.setAddress(toAddress(request.address()));
    customer.setPrivacySettings(toPrivacySettings(request.privacySettings()));
    replaceEmails(customer, request.emails());
    replacePhoneNumbers(customer, request.phoneNumbers());

    Customer saved = repository.save(customer);
    return toResponse(saved);
  }

  @Transactional
  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(request, "request");
    validateUniqueEmails(request.emails());

    Customer existing = repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));

    applyFields(existing, request.firstName(), request.middleName(), request.lastName());
    existing.setAddress(toAddress(request.address()));
    existing.setPrivacySettings(toPrivacySettings(request.privacySettings()));
    replaceEmails(existing, request.emails());
    replacePhoneNumbers(existing, request.phoneNumbers());

    Customer updated = repository.save(existing);
    return toResponse(updated);
  }

  @Transactional
  public CustomerDto.Response get(UUID id) {
    Objects.requireNonNull(id, "id");
    Customer customer =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
    return toResponse(customer);
  }

  @Transactional
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public void delete(UUID id) {
    Objects.requireNonNull(id, "id");
    Customer customer =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
    repository.delete(customer);
  }

  private void applyFields(Customer customer, String firstName, String middleName, String lastName) {
    customer.setFirstName(firstName);
    customer.setMiddleName(middleName);
    customer.setLastName(lastName);
  }

  private void replaceEmails(Customer customer, List<String> emails) {
    customer.clearEmails();
    for (String emailValue : emails) {
      CustomerEmail email = new CustomerEmail();
      email.setEmail(emailValue);
      customer.addEmail(email);
    }
  }

  private void replacePhoneNumbers(Customer customer, List<PhoneNumberDto> phoneNumbers) {
    customer.clearPhoneNumbers();
    for (PhoneNumberDto dto : phoneNumbers) {
      CustomerPhoneNumber phone = new CustomerPhoneNumber();
      phone.setType(dto.type());
      phone.setNumber(dto.number());
      customer.addPhoneNumber(phone);
    }
  }

  private PostalAddress toAddress(AddressDto dto) {
    if (dto == null) {
      return null;
    }
    PostalAddress address = new PostalAddress();
    address.setLine1(dto.line1());
    address.setLine2(dto.line2());
    address.setCity(dto.city());
    address.setState(dto.state());
    address.setPostalCode(dto.postalCode());
    address.setCountry(dto.country());
    return address;
  }

  private PrivacySettings toPrivacySettings(PrivacySettingsDto dto) {
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(dto.marketingEmailsEnabled());
    settings.setTwoFactorEnabled(dto.twoFactorEnabled());
    return settings;
  }

  private CustomerDto.Response toResponse(Customer customer) {
    List<String> emails = customer.getEmails().stream().map(CustomerEmail::getEmail).toList();
    List<PhoneNumberDto> phoneNumbers =
        customer.getPhoneNumbers().stream()
            .map(phone -> new PhoneNumberDto(phone.getType(), phone.getNumber()))
            .toList();

    AddressDto address = null;
    if (customer.getAddress() != null) {
      PostalAddress entity = customer.getAddress();
      address =
          new AddressDto(
              entity.getLine1(),
              entity.getLine2(),
              entity.getCity(),
              entity.getState(),
              entity.getPostalCode(),
              entity.getCountry());
    }

    PrivacySettingsDto privacy = null;
    if (customer.getPrivacySettings() != null) {
      PrivacySettings entity = customer.getPrivacySettings();
      privacy = new PrivacySettingsDto(entity.getMarketingEmailsEnabled(), entity.getTwoFactorEnabled());
    }

    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        emails,
        phoneNumbers,
        address,
        privacy);
  }

  private void validateUniqueEmails(List<String> emails) {
    Set<String> normalized =
        emails.stream()
            .map(email -> email == null ? null : email.toLowerCase())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    if (normalized.size() != emails.size()) {
      throw new IllegalArgumentException("Emails must be unique per customer");
    }
  }
}
