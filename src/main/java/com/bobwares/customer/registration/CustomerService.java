/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerService
 * Description: Transactional service encapsulating Customer CRUD operations.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.CustomerEmail;
import com.bobwares.customer.registration.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.model.PhoneType;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public CustomerDto.Response create(@Valid @NotNull CustomerDto.CreateRequest request) {
    validateUniqueEmails(request.emails(), null);
    Customer customer = new Customer();
    applyRequestToCustomer(customer, request.firstName(), request.middleName(), request.lastName(), request.address(), request.privacySettings(), request.emails(), request.phoneNumbers());
    Customer saved = customerRepository.save(customer);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public CustomerDto.Response get(@NotNull UUID id) {
    Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
    return toResponse(customer);
  }

  @Transactional(readOnly = true)
  public List<CustomerDto.Response> list() {
    return customerRepository.findAll().stream()
        .sorted(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName))
        .map(this::toResponse)
        .toList();
  }

  public CustomerDto.Response update(@NotNull UUID id, @Valid @NotNull CustomerDto.UpdateRequest request) {
    Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
    validateUniqueEmails(request.emails(), id);
    applyRequestToCustomer(customer, request.firstName(), request.middleName(), request.lastName(), request.address(), request.privacySettings(), request.emails(), request.phoneNumbers());
    return toResponse(customer);
  }

  public void delete(@NotNull UUID id) {
    if (!customerRepository.existsById(id)) {
      throw new EntityNotFoundException("Customer %s not found".formatted(id));
    }
    customerRepository.deleteById(id);
  }

  private void applyRequestToCustomer(
      Customer customer,
      String firstName,
      String middleName,
      String lastName,
      CustomerDto.Address addressDto,
      CustomerDto.PrivacySettings privacyDto,
      List<CustomerDto.EmailAddress> emails,
      List<CustomerDto.PhoneNumber> phoneNumbers) {

    customer.setFirstName(firstName.trim());
    customer.setMiddleName(StringUtils.hasText(middleName) ? middleName.trim() : null);
    customer.setLastName(lastName.trim());

    PostalAddress address = customer.getAddress() != null ? customer.getAddress() : new PostalAddress();
    address.setLine1(addressDto.line1().trim());
    address.setLine2(StringUtils.hasText(addressDto.line2()) ? addressDto.line2().trim() : null);
    address.setCity(addressDto.city().trim());
    address.setState(addressDto.state().trim());
    address.setPostalCode(addressDto.postalCode().trim());
    address.setCountry(addressDto.country().trim().toUpperCase(Locale.US));
    customer.setAddress(address);

    PrivacySettings privacy = customer.getPrivacySettings() != null ? customer.getPrivacySettings() : new PrivacySettings();
    privacy.setMarketingEmailsEnabled(privacyDto.marketingEmailsEnabled());
    privacy.setTwoFactorEnabled(privacyDto.twoFactorEnabled());
    customer.setPrivacySettings(privacy);

    customer.clearEmails();
    emails.forEach(emailDto -> {
      CustomerEmail email = new CustomerEmail();
      email.setEmail(emailDto.value().trim().toLowerCase(Locale.US));
      customer.addEmail(email);
    });

    customer.clearPhoneNumbers();
    phoneNumbers.forEach(phoneDto -> {
      CustomerPhoneNumber phone = new CustomerPhoneNumber();
      phone.setType(normalizePhoneType(phoneDto.type()));
      phone.setNumber(phoneDto.number().trim());
      customer.addPhoneNumber(phone);
    });
  }

  private void validateUniqueEmails(List<CustomerDto.EmailAddress> emails, UUID currentCustomerId) {
    Set<String> normalized = emails.stream()
        .map(email -> email.value().trim().toLowerCase(Locale.US))
        .collect(Collectors.toSet());
    if (normalized.size() != emails.size()) {
      throw new IllegalArgumentException("Duplicate email addresses supplied");
    }
    for (String email : normalized) {
      boolean exists = currentCustomerId == null
          ? customerRepository.existsByEmailsEmailIgnoreCase(email)
          : customerRepository.existsByIdNotAndEmailsEmailIgnoreCase(currentCustomerId, email);
      if (exists) {
        throw new IllegalArgumentException("Email address %s already registered".formatted(email));
      }
    }
  }

  private CustomerDto.Response toResponse(Customer customer) {
    CustomerDto.Address address = new CustomerDto.Address(
        customer.getAddress().getLine1(),
        customer.getAddress().getLine2(),
        customer.getAddress().getCity(),
        customer.getAddress().getState(),
        customer.getAddress().getPostalCode(),
        customer.getAddress().getCountry());

    CustomerDto.PrivacySettings privacySettings =
        new CustomerDto.PrivacySettings(
            customer.getPrivacySettings().getMarketingEmailsEnabled(),
            customer.getPrivacySettings().getTwoFactorEnabled());

    List<String> emails = customer.getEmails().stream()
        .map(CustomerEmail::getEmail)
        .sorted()
        .toList();

    List<CustomerDto.PhoneNumber> phoneNumbers = customer.getPhoneNumbers().stream()
        .sorted(Comparator.comparing(CustomerPhoneNumber::getType).thenComparing(CustomerPhoneNumber::getNumber))
        .map(phone -> new CustomerDto.PhoneNumber(phone.getType(), phone.getNumber()))
        .toList();

    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        address,
        privacySettings,
        emails,
        phoneNumbers);
  }

  private PhoneType normalizePhoneType(PhoneType type) {
    return type == null ? PhoneType.OTHER : type;
  }
}
