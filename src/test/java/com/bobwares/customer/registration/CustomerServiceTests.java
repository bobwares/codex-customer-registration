/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerServiceTests
 * Description: Unit tests for CustomerService covering CRUD flows and validation safeguards.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerDto.AddressDto;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumberDto;
import com.bobwares.customer.registration.api.CustomerDto.PrivacySettingsDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.CustomerEmail;
import com.bobwares.customer.registration.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.model.PhoneType;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

  @Mock private CustomerRepository repository;

  private CustomerService service;

  @BeforeEach
  void setUp() {
    service = new CustomerService(repository);
  }

  @Test
  void createPersistsCustomer() {
    CustomerDto.CreateRequest request = sampleCreateRequest();
    Customer saved = sampleCustomer();
    when(repository.save(org.mockito.Mockito.any(Customer.class))).thenReturn(saved);

    CustomerDto.Response response = service.create(request);

    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(repository).save(captor.capture());
    Customer persisted = captor.getValue();
    assertThat(persisted.getFirstName()).isEqualTo("Alice");
    assertThat(persisted.getEmails()).hasSize(1);
    assertThat(response.firstName()).isEqualTo("Alice");
    assertThat(response.emails()).containsExactly("alice@example.com");
  }

  @Test
  void createWithDuplicateEmailsThrows() {
    CustomerDto.CreateRequest request =
        new CustomerDto.CreateRequest(
            "Alice",
            null,
            "Smith",
            List.of("alice@example.com", "alice@example.com"),
            List.of(new PhoneNumberDto(PhoneType.MOBILE, "+12025550123")),
            new AddressDto("123 Main", null, "City", "ST", "12345", "US"),
            new PrivacySettingsDto(true, true));

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Emails must be unique");
  }

  @Test
  void getReturnsCustomerResponse() {
    Customer entity = sampleCustomer();
    UUID id = UUID.randomUUID();
    entity.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(entity));

    CustomerDto.Response response = service.get(id);

    assertThat(response.id()).isEqualTo(id);
    assertThat(response.privacySettings().twoFactorEnabled()).isTrue();
  }

  @Test
  void getMissingCustomerThrows() {
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(id)).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void updateWritesChangesToRepository() {
    UUID id = UUID.randomUUID();
    Customer existing = sampleCustomer();
    existing.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);

    CustomerDto.UpdateRequest request =
        new CustomerDto.UpdateRequest(
            "Bob",
            "Q",
            "Jones",
            List.of("bob@example.com"),
            List.of(new PhoneNumberDto(PhoneType.HOME, "+12025550124")),
            new AddressDto("456 Elm", "Apt 2", "Town", "TS", "67890", "US"),
            new PrivacySettingsDto(false, true));

    CustomerDto.Response response = service.update(id, request);

    verify(repository).save(existing);
    assertThat(response.firstName()).isEqualTo("Bob");
    assertThat(existing.getEmails()).extracting(CustomerEmail::getEmail).containsExactly("bob@example.com");
  }

  @Test
  void deleteRemovesCustomer() {
    UUID id = UUID.randomUUID();
    Customer existing = sampleCustomer();
    existing.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(existing));
    doNothing().when(repository).delete(existing);

    service.delete(id);

    verify(repository).delete(existing);
  }

  private Customer sampleCustomer() {
    Customer customer = new Customer();
    customer.setFirstName("Alice");
    customer.setLastName("Smith");
    customer.setMiddleName("B");

    PostalAddress address = new PostalAddress();
    address.setLine1("123 Main");
    address.setCity("City");
    address.setState("ST");
    address.setPostalCode("12345");
    address.setCountry("US");
    customer.setAddress(address);

    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(true);
    settings.setTwoFactorEnabled(true);
    customer.setPrivacySettings(settings);

    CustomerEmail email = new CustomerEmail();
    email.setEmail("alice@example.com");
    customer.addEmail(email);

    CustomerPhoneNumber phone = new CustomerPhoneNumber();
    phone.setType(PhoneType.MOBILE);
    phone.setNumber("+12025550123");
    customer.addPhoneNumber(phone);

    return customer;
  }

  private CustomerDto.CreateRequest sampleCreateRequest() {
    return new CustomerDto.CreateRequest(
        "Alice",
        "B",
        "Smith",
        List.of("alice@example.com"),
        List.of(new PhoneNumberDto(PhoneType.MOBILE, "+12025550123")),
        new AddressDto("123 Main", null, "City", "ST", "12345", "US"),
        new PrivacySettingsDto(true, true));
  }
}
