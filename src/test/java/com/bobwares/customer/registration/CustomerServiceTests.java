/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerServiceTests
 * Description: Validates CustomerService CRUD flows and business rules using PostgreSQL Testcontainers.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.support.PostgresContainerSupport;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CustomerService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceTests extends PostgresContainerSupport {

  @Autowired private CustomerService service;
  @Autowired private CustomerRepository repository;

  @BeforeEach
  void clean() {
    repository.deleteAll();
  }

  @Test
  void createPersistsCustomer() {
    Customer created = service.create(sampleCreateRequest("alice@example.com"));

    assertThat(created.getId()).isNotNull();
    assertThat(created.getEmails()).containsExactly("alice@example.com");
    assertThat(created.getPrivacySettings().isTwoFactorEnabled()).isTrue();
  }

  @Test
  void updateReplacesMutableFields() {
    Customer created = service.create(sampleCreateRequest("bob@example.com"));

    CustomerDto.UpdateRequest updateRequest =
        new CustomerDto.UpdateRequest(
            "Robert",
            created.getMiddleName(),
            "Builder",
            List.of("bob@example.com"),
            List.of(new CustomerDto.PhoneNumberDto("mobile", "+18005550102")),
            new CustomerDto.AddressDto("1 Main", null, "Austin", "TX", "73301", "US"),
            new CustomerDto.PrivacySettingsDto(false, true));

    Customer updated = service.update(created.getId(), updateRequest);

    assertThat(updated.getFirstName()).isEqualTo("Robert");
    assertThat(updated.getAddress()).isNotNull();
    assertThat(updated.getAddress().getCity()).isEqualTo("Austin");
  }

  @Test
  void deleteRemovesCustomer() {
    Customer created = service.create(sampleCreateRequest("eve@example.com"));

    service.delete(created.getId());

    assertThat(repository.findById(created.getId())).isEmpty();
  }

  @Test
  void duplicateEmailRejectedAcrossCustomers() {
    service.create(sampleCreateRequest("dup@example.com"));

    assertThatThrownBy(() -> service.create(sampleCreateRequest("dup@example.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already registered");
  }

  private CustomerDto.CreateRequest sampleCreateRequest(String email) {
    return new CustomerDto.CreateRequest(
        "Alice",
        "Q",
        "Example",
        List.of(email),
        List.of(new CustomerDto.PhoneNumberDto("mobile", "+18005550101")),
        new CustomerDto.AddressDto("123 Market", "Suite 100", "San Francisco", "CA", "94105", "US"),
        new CustomerDto.PrivacySettingsDto(true, true));
  }
}
