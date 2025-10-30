/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerService
 * Description: Provides transactional CRUD workflows for customer profiles with validation of unique contact data.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.Customer.Address;
import com.bobwares.customer.registration.Customer.PhoneNumber;
import com.bobwares.customer.registration.Customer.PhoneType;
import com.bobwares.customer.registration.Customer.PrivacySettings;
import com.bobwares.customer.registration.api.CustomerDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Customer create(CustomerDto.CreateRequest request) {
    Customer customer = new Customer();
    copyIntoAggregate(customer, request.firstName(), request.middleName(), request.lastName(),
        request.emails(), request.phoneNumbers(), request.address(), request.privacySettings());
    ensureEmailUniqueness(customer.getEmails(), null);
    return repository.save(customer);
  }

  @Transactional
  public Customer update(UUID id, CustomerDto.UpdateRequest request) {
    Customer existing =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));

    copyIntoAggregate(existing, request.firstName(), request.middleName(), request.lastName(),
        request.emails(), request.phoneNumbers(), request.address(), request.privacySettings());
    ensureEmailUniqueness(existing.getEmails(), existing.getId());
    return repository.save(existing);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Customer not found: " + id);
    }
    repository.deleteById(id);
  }

  public Customer get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
  }

  public List<Customer> list() {
    return repository.findAll(Sort.by(Sort.Order.asc("lastName"), Sort.Order.asc("firstName")));
  }

  private void copyIntoAggregate(
      Customer customer,
      String firstName,
      String middleName,
      String lastName,
      List<String> emails,
      List<CustomerDto.PhoneNumberDto> phoneNumbers,
      CustomerDto.AddressDto addressDto,
      CustomerDto.PrivacySettingsDto privacyDto) {
    LinkedHashSet<String> uniqueEmails = normaliseEmails(emails);
    customer.setFirstName(firstName);
    customer.setMiddleName(middleName);
    customer.setLastName(lastName);
    customer.setEmails(uniqueEmails);

    LinkedHashSet<PhoneNumber> numbers =
        phoneNumbers == null
            ? new LinkedHashSet<>()
            : phoneNumbers.stream()
                .map(this::toPhoneNumber)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    customer.setPhoneNumbers(numbers);

    if (addressDto != null) {
      Address address = customer.getAddress();
      if (address == null) {
        address = new Address();
      }
      address.setLine1(addressDto.line1());
      address.setLine2(addressDto.line2());
      address.setCity(addressDto.city());
      address.setState(addressDto.state());
      address.setPostalCode(addressDto.postalCode());
      address.setCountry(addressDto.country());
      customer.setAddress(address);
    } else {
      customer.setAddress(null);
    }

    PrivacySettings privacySettings = customer.getPrivacySettings();
    if (privacySettings == null) {
      privacySettings = new PrivacySettings();
    }
    privacySettings.setMarketingEmailsEnabled(privacyDto.marketingEmailsEnabled());
    privacySettings.setTwoFactorEnabled(privacyDto.twoFactorEnabled());
    customer.setPrivacySettings(privacySettings);
  }

  private LinkedHashSet<String> normaliseEmails(List<String> emails) {
    if (emails == null || emails.isEmpty()) {
      throw new IllegalArgumentException("At least one email address is required");
    }
    LinkedHashSet<String> uniqueEmails =
        emails.stream()
            .map(email -> email == null ? null : email.trim())
            .map(email -> email == null ? null : email.toLowerCase(Locale.ROOT))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    if (uniqueEmails.contains(null)) {
      throw new IllegalArgumentException("Email addresses may not be null");
    }
    if (uniqueEmails.size() != emails.size()) {
      throw new IllegalArgumentException("Duplicate email addresses are not allowed");
    }
    return uniqueEmails;
  }

  private void ensureEmailUniqueness(Set<String> emails, UUID excludeId) {
    for (String email : emails) {
      if (repository.existsByEmailIgnoreCase(email, excludeId)) {
        throw new IllegalArgumentException("Email address already registered: " + email);
      }
    }
  }

  private PhoneNumber toPhoneNumber(CustomerDto.PhoneNumberDto dto) {
    PhoneNumber phoneNumber = new PhoneNumber();
    phoneNumber.setType(PhoneType.valueOf(dto.type().toUpperCase(Locale.ROOT)));
    phoneNumber.setNumber(dto.number());
    return phoneNumber;
  }
}
