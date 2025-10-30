/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: CustomerService
 * Description: Application service orchestrating CRUD operations and uniqueness rules for customers.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.PhoneNumber;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
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

  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    ensureUniqueEmails(request.emails(), null);
    Customer customer = new Customer();
    applyRequest(customer, request.emails(), request.phoneNumbers(), request.address(), request.privacySettings());
    customer.setFirstName(request.firstName());
    customer.setMiddleName(request.middleName());
    customer.setLastName(request.lastName());
    return toResponse(repository.save(customer));
  }

  public CustomerDto.Response get(UUID id) {
    return toResponse(findOrFail(id));
  }

  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    Customer existing = findOrFail(id);
    ensureUniqueEmails(request.emails(), id);
    existing.setFirstName(request.firstName());
    existing.setMiddleName(request.middleName());
    existing.setLastName(request.lastName());
    applyRequest(existing, request.emails(), request.phoneNumbers(), request.address(), request.privacySettings());
    return toResponse(existing);
  }

  public void delete(UUID id) {
    Customer customer = findOrFail(id);
    repository.delete(customer);
  }

  private Customer findOrFail(UUID id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
  }

  private void ensureUniqueEmails(Set<String> emails, UUID currentId) {
    for (String email : emails) {
      repository
          .findByEmails(email)
          .filter(existing -> !Objects.equals(existing.getId(), currentId))
          .ifPresent(
              existing -> {
                throw new IllegalArgumentException("Email already registered: " + email);
              });
    }
  }

  private void applyRequest(
      Customer customer,
      Set<String> emails,
      List<CustomerDto.PhoneNumberPayload> phoneNumbers,
      CustomerDto.AddressPayload addressPayload,
      CustomerDto.PrivacyPayload privacyPayload) {
    customer.setEmails(new LinkedHashSet<>(emails));
    if (phoneNumbers != null) {
      customer.setPhoneNumbers(
          phoneNumbers.stream().map(this::toPhoneNumber).toList());
    } else {
      customer.setPhoneNumbers(null);
    }
    if (addressPayload != null) {
      customer.setAddress(toAddress(addressPayload));
    } else {
      customer.setAddress(null);
    }
    customer.setPrivacySettings(toPrivacy(privacyPayload));
  }

  private PhoneNumber toPhoneNumber(CustomerDto.PhoneNumberPayload payload) {
    PhoneNumber phoneNumber = new PhoneNumber();
    phoneNumber.setType(payload.type());
    phoneNumber.setNumber(payload.number());
    return phoneNumber;
  }

  private PostalAddress toAddress(CustomerDto.AddressPayload payload) {
    PostalAddress address = new PostalAddress();
    address.setLine1(payload.line1());
    address.setLine2(payload.line2());
    address.setCity(payload.city());
    address.setState(payload.state());
    address.setPostalCode(payload.postalCode());
    address.setCountry(payload.country());
    return address;
  }

  private PrivacySettings toPrivacy(CustomerDto.PrivacyPayload payload) {
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(payload.marketingEmailsEnabled());
    settings.setTwoFactorEnabled(payload.twoFactorEnabled());
    return settings;
  }

  private CustomerDto.Response toResponse(Customer customer) {
    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        customer.getEmails(),
        customer.getPhoneNumbers() == null
            ? null
            : customer.getPhoneNumbers().stream()
                .map(pn -> new CustomerDto.PhoneNumberPayload(pn.getType(), pn.getNumber()))
                .toList(),
        customer.getAddress() == null
            ? null
            : new CustomerDto.AddressPayload(
                customer.getAddress().getLine1(),
                customer.getAddress().getLine2(),
                customer.getAddress().getCity(),
                customer.getAddress().getState(),
                customer.getAddress().getPostalCode(),
                customer.getAddress().getCountry()),
        new CustomerDto.PrivacyPayload(
            customer.getPrivacySettings().getMarketingEmailsEnabled(),
            customer.getPrivacySettings().getTwoFactorEnabled()),
        customer.getCreatedAt(),
        customer.getUpdatedAt());
  }
}
