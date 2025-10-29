/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerServiceTests
 * Description: Validates customer service operations and uniqueness enforcement using an in-memory H2 database configured in PostgreSQL compatibility mode.
 */
package com.bobwares.customer.registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.PhoneNumberType;
import com.bobwares.customer.registration.api.CustomerDto;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:customer_service_tests;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
      "spring.liquibase.enabled=false"
    })
class CustomerServiceTests {

  @Autowired private CustomerService service;

  @Test
  void createCustomerPersistsAndGeneratesPrimaryEmail() {
    Customer saved = service.create(buildCreateRequest("primary@example.com", "backup@example.com"));
    assertNotNull(saved.getId());
    assertEquals("primary@example.com", saved.getPrimaryEmail());
    assertTrue(saved.getEmails().contains("backup@example.com"));
  }

  @Test
  void duplicateEmailThrowsException() {
    service.create(buildCreateRequest("duplicate@example.com"));
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.create(buildCreateRequest("duplicate@example.com")));
    assertTrue(ex.getMessage().contains("duplicate@example.com"));
  }

  @Test
  void updateCustomerChangesNames() {
    Customer saved = service.create(buildCreateRequest("update@example.com"));
    CustomerDto.UpdateRequest updateRequest =
        new CustomerDto.UpdateRequest(
            "New",
            "Middle",
            "Name",
            Set.of("update@example.com"),
            new CustomerDto.PostalAddressRequest("123", null, "City", "State", "12345", "US"),
            new CustomerDto.PrivacySettingsRequest(true, false),
            List.of(new CustomerDto.PhoneNumberRequest(PhoneNumberType.MOBILE, "+18005550100")));

    Customer updated = service.update(saved.getId(), updateRequest);
    assertEquals("New", updated.getFirstName());
    assertEquals("Middle", updated.getMiddleName());
    assertEquals("Name", updated.getLastName());
  }

  private CustomerDto.CreateRequest buildCreateRequest(String primary, String... additional) {
    Set<String> emails = new java.util.LinkedHashSet<>();
    emails.add(primary);
    if (additional.length > 0) {
      emails.add(additional[0]);
    }
    List<CustomerDto.PhoneNumberRequest> phones =
        List.of(new CustomerDto.PhoneNumberRequest(PhoneNumberType.MOBILE, "+18005550100"));
    return new CustomerDto.CreateRequest(
        "Alice",
        "M",
        "Example",
        emails,
        new CustomerDto.PostalAddressRequest("123", null, "City", "State", "12345", "US"),
        new CustomerDto.PrivacySettingsRequest(true, true),
        phones);
  }
}
