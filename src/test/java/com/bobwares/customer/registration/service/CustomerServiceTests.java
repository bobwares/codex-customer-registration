/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerServiceTests
 * Description: Unit tests covering CustomerService CRUD flows and uniqueness validation logic.
 */
package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.bobwares.customer.registration.api.CustomerDto.AddressDto;
import com.bobwares.customer.registration.api.CustomerDto.CreateRequest;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumberDto;
import com.bobwares.customer.registration.api.CustomerDto.PrivacySettingsDto;
import com.bobwares.customer.registration.api.CustomerDto.Response;
import com.bobwares.customer.registration.model.PhoneNumberType;
import com.bobwares.customer.registration.persistence.Customer;
import com.bobwares.customer.registration.persistence.CustomerEmail;
import com.bobwares.customer.registration.persistence.CustomerPhoneNumber;
import com.bobwares.customer.registration.persistence.CustomerRepository;
import com.bobwares.customer.registration.persistence.PrivacySettings;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomerServiceTests {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createPersistsCustomer() {
    CreateRequest request = sampleCreateRequest();
    Customer saved = new Customer();
    saved.setFirstName("Alice");
    saved.setLastName("Smith");
    saved.setPrivacySettings(new PrivacySettings());
    saved.setEmails(Set.of(new CustomerEmail()));
    saved.setPhoneNumbers(Set.of(new CustomerPhoneNumber()));
    given(repository.existsByEmailsValueIgnoreCase(anyString())).willReturn(false);
    given(repository.existsByPhoneNumbersNumber(anyString())).willReturn(false);
    given(repository.save(any(Customer.class))).willReturn(saved);

    Response response = service.create(request);

    assertThat(response.firstName()).isEqualTo("Alice");
    verify(repository).save(any(Customer.class));
  }

  @Test
  void createThrowsWhenEmailExists() {
    CreateRequest request = sampleCreateRequest();
    given(repository.existsByEmailsValueIgnoreCase("user@example.com")).willReturn(true);

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email already in use");
  }

  @Test
  void getThrowsWhenMissing() {
    UUID id = UUID.randomUUID();
    assertThatThrownBy(() -> service.get(id))
        .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
        .hasMessageContaining("Customer not found");
  }

  private CreateRequest sampleCreateRequest() {
    return new CreateRequest(
        "Alice",
        null,
        "Smith",
        new PrivacySettingsDto(true, true),
        new AddressDto("123 Main", null, "City", "State", "12345", "US"),
        List.of("user@example.com"),
        List.of(new PhoneNumberDto(PhoneNumberType.MOBILE, "+15551234567")));
  }
}
