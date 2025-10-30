/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: CustomerServiceTests
 * Description: Unit tests covering core behaviors of the CustomerService component.
 */
package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerRepository;
import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.PhoneType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    when(repository.findByEmails("user@example.com")).thenReturn(Optional.empty());
    when(repository.save(any(Customer.class)))
        .thenAnswer(
            invocation -> {
              Customer customer = invocation.getArgument(0);
              customer.setId(UUID.randomUUID());
              return customer;
            });

    CustomerDto.Response response = service.create(sampleCreateRequest());

    assertThat(response.id()).isNotNull();
  }

  @Test
  void createRejectsDuplicateEmail() {
    Customer existing = new Customer();
    existing.setId(UUID.randomUUID());
    when(repository.findByEmails("user@example.com")).thenReturn(Optional.of(existing));

    assertThatThrownBy(() -> service.create(sampleCreateRequest()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email already registered");
  }

  private CustomerDto.CreateRequest sampleCreateRequest() {
    return new CustomerDto.CreateRequest(
        "Jane",
        null,
        "Doe",
        Set.of("user@example.com"),
        List.of(new CustomerDto.PhoneNumberPayload(PhoneType.MOBILE, "+15551234567")),
        new CustomerDto.AddressPayload("123 Main", null, "City", "State", "12345", "US"),
        new CustomerDto.PrivacyPayload(true, true));
  }
}
