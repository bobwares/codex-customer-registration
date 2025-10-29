/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerService
 * Description: Transactional service orchestrating persistence and validation for customer CRUD operations.
 */
package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerDto.AddressDto;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumberDto;
import com.bobwares.customer.registration.api.CustomerDto.PrivacySettingsDto;
import com.bobwares.customer.registration.persistence.Customer;
import com.bobwares.customer.registration.persistence.CustomerAddress;
import com.bobwares.customer.registration.persistence.CustomerEmail;
import com.bobwares.customer.registration.persistence.CustomerPhoneNumber;
import com.bobwares.customer.registration.persistence.CustomerRepository;
import com.bobwares.customer.registration.persistence.PrivacySettings;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  public CustomerDto.Response create(@Valid @NotNull CustomerDto.CreateRequest request) {
    validateUniquenessForCreate(request);
    Customer entity = toEntity(new Customer(), request);
    Customer saved = repository.save(entity);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public CustomerDto.Response get(UUID id) {
    Customer customer = repository.findById(id).orElseThrow(() -> notFound(id));
    return toResponse(customer);
  }

  @Transactional(readOnly = true)
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream()
        .sorted(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName))
        .map(this::toResponse)
        .toList();
  }

  public CustomerDto.Response update(UUID id, @Valid @NotNull CustomerDto.UpdateRequest request) {
    Customer customer = repository.findById(id).orElseThrow(() -> notFound(id));
    validateUniquenessForUpdate(id, request);
    Customer updated = toEntity(customer, request);
    Customer saved = repository.save(updated);
    return toResponse(saved);
  }

  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw notFound(id);
    }
    repository.deleteById(id);
  }

  private void validateUniquenessForCreate(CustomerDto.CreateRequest request) {
    request.emails().forEach(
        email -> {
          if (repository.existsByEmailsValueIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
          }
        });
    request.phoneNumbers().forEach(
        phone -> {
          if (repository.existsByPhoneNumbersNumber(phone.number())) {
            throw new IllegalArgumentException("Phone number already in use: " + phone.number());
          }
        });
  }

  private void validateUniquenessForUpdate(UUID id, CustomerDto.UpdateRequest request) {
    request.emails().forEach(
        email -> {
          if (repository.existsByEmailsValueIgnoreCaseAndIdNot(email, id)) {
            throw new IllegalArgumentException("Email already in use: " + email);
          }
        });
    request.phoneNumbers().forEach(
        phone -> {
          if (repository.existsByPhoneNumbersNumberAndIdNot(phone.number(), id)) {
            throw new IllegalArgumentException("Phone number already in use: " + phone.number());
          }
        });
  }

  private Customer toEntity(Customer customer, CustomerDto.CreateRequest request) {
    customer.setFirstName(request.firstName());
    customer.setMiddleName(request.middleName());
    customer.setLastName(request.lastName());
    customer.setPrivacySettings(toPrivacySettings(customer.getPrivacySettings(), request.privacySettings()));
    customer.setAddress(toAddress(customer.getAddress(), request.address()));
    customer.setEmails(toEmailEntities(request.emails()));
    customer.setPhoneNumbers(toPhoneEntities(request.phoneNumbers()));
    return customer;
  }

  private Customer toEntity(Customer customer, CustomerDto.UpdateRequest request) {
    customer.setFirstName(request.firstName());
    customer.setMiddleName(request.middleName());
    customer.setLastName(request.lastName());
    customer.setPrivacySettings(toPrivacySettings(customer.getPrivacySettings(), request.privacySettings()));
    customer.setAddress(toAddress(customer.getAddress(), request.address()));
    customer.setEmails(toEmailEntities(request.emails()));
    customer.setPhoneNumbers(toPhoneEntities(request.phoneNumbers()));
    return customer;
  }

  private PrivacySettings toPrivacySettings(PrivacySettings existing, PrivacySettingsDto dto) {
    PrivacySettings privacy = existing == null ? new PrivacySettings() : existing;
    privacy.setMarketingEmailsEnabled(dto.marketingEmailsEnabled());
    privacy.setTwoFactorEnabled(dto.twoFactorEnabled());
    return privacy;
  }

  private CustomerAddress toAddress(CustomerAddress existing, AddressDto dto) {
    if (dto == null) {
      return null;
    }
    CustomerAddress address = existing == null ? new CustomerAddress() : existing;
    address.setLine1(dto.line1());
    address.setLine2(dto.line2());
    address.setCity(dto.city());
    address.setState(dto.state());
    address.setPostalCode(dto.postalCode());
    address.setCountry(dto.country());
    return address;
  }

  private Set<CustomerEmail> toEmailEntities(List<String> emails) {
    return emails.stream()
        .map(
            email -> {
              CustomerEmail entity = new CustomerEmail();
              entity.setValue(email);
              return entity;
            })
        .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
  }

  private Set<CustomerPhoneNumber> toPhoneEntities(List<PhoneNumberDto> phoneNumbers) {
    return phoneNumbers.stream()
        .map(
            dto -> {
              CustomerPhoneNumber entity = new CustomerPhoneNumber();
              entity.setType(dto.type());
              entity.setNumber(dto.number());
              return entity;
            })
        .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
  }

  private CustomerDto.Response toResponse(Customer customer) {
    PrivacySettings privacy = customer.getPrivacySettings();
    PrivacySettingsDto privacyDto =
        new PrivacySettingsDto(privacy.getMarketingEmailsEnabled(), privacy.getTwoFactorEnabled());
    AddressDto addressDto = null;
    if (customer.getAddress() != null) {
      CustomerAddress address = customer.getAddress();
      addressDto =
          new AddressDto(
              address.getLine1(),
              address.getLine2(),
              address.getCity(),
              address.getState(),
              address.getPostalCode(),
              address.getCountry());
    }
    List<String> emails =
        customer.getEmails().stream()
            .map(CustomerEmail::getValue)
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();
    List<PhoneNumberDto> phones =
        customer.getPhoneNumbers().stream()
            .sorted(Comparator.comparing(CustomerPhoneNumber::getNumber))
            .map(phone -> new PhoneNumberDto(phone.getType(), phone.getNumber()))
            .toList();
    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        privacyDto,
        addressDto,
        emails,
        phones,
        customer.getCreatedAt(),
        customer.getUpdatedAt());
  }

  private EntityNotFoundException notFound(UUID id) {
    return new EntityNotFoundException("Customer not found: " + id);
  }
}
