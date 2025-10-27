/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerServiceTests
 * Description: Verifies CustomerService transactional behaviour and validation rules.
 */
package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bobwares.customer.registration.CustomerRepository;
import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.PhoneType;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import(CustomerService.class)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:customer_service_tests;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.liquibase.enabled=false",
    "APP_NAME=customer-registration-test",
    "APP_PORT=8080",
    "APP_DEFAULT_TAX_RATE=0.0000",
    "APP_DEFAULT_SHIPPING_COST=0.00",
    "APP_SUPPORTED_CURRENCIES=USD,EUR"
})
class CustomerServiceTests {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Test
  void createCustomerPersistsAggregate() {
    CustomerDto.Response response = customerService.create(sampleCreateRequest("alice@example.com"));

    assertThat(response.id()).isNotNull();
    assertThat(customerRepository.count()).isEqualTo(1);
    assertThat(response.emails()).containsExactly("alice@example.com");
  }

  @Test
  void createCustomerRejectsDuplicateEmail() {
    customerService.create(sampleCreateRequest("alice@example.com"));

    assertThrows(IllegalArgumentException.class, () -> customerService.create(sampleCreateRequest("alice@example.com")));
  }

  @Test
  void updateCustomerReplacesContacts() {
    CustomerDto.Response created = customerService.create(sampleCreateRequest("alice@example.com"));

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest(
        "Alice",
        "J",
        "Miller",
        sampleAddress(),
        samplePrivacy(),
        List.of(new CustomerDto.EmailAddress("alice.updated@example.com")),
        List.of(new CustomerDto.PhoneNumber(PhoneType.MOBILE, "+15559999999")));

    CustomerDto.Response updated = customerService.update(created.id(), updateRequest);

    assertThat(updated.emails()).containsExactly("alice.updated@example.com");
    assertThat(updated.phoneNumbers()).hasSize(1);
  }

  @Test
  void getUnknownCustomerThrows() {
    assertThrows(EntityNotFoundException.class, () -> customerService.get(UUID.randomUUID()));
  }

  private CustomerDto.CreateRequest sampleCreateRequest(String email) {
    return new CustomerDto.CreateRequest(
        "Alice",
        "J",
        "Miller",
        sampleAddress(),
        samplePrivacy(),
        List.of(new CustomerDto.EmailAddress(email)),
        List.of(new CustomerDto.PhoneNumber(PhoneType.MOBILE, "+15551234567")));
  }

  private CustomerDto.Address sampleAddress() {
    return new CustomerDto.Address("100 Main St", null, "Springfield", "IL", "62701", "US");
  }

  private CustomerDto.PrivacySettings samplePrivacy() {
    return new CustomerDto.PrivacySettings(true, true);
  }
}
