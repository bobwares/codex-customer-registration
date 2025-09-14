/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: none
 * Description: Unit tests for CustomerService using Testcontainers PostgreSQL.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.api.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class CustomerServiceTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    CustomerService service;

    @Test
    void createAndGetCustomer() {
        CustomerDto.CreateRequest req = new CustomerDto.CreateRequest();
        req.setFirstName("Alice");
        req.setLastName("Smith");
        req.setEmails(List.of("alice@example.com"));
        CustomerDto.PrivacySettingsDto ps = new CustomerDto.PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        req.setPrivacySettings(ps);
        Customer created = service.create(CustomerDto.toEntity(req));
        Customer fetched = service.get(created.getId());
        assertEquals("Alice", fetched.getFirstName());
    }

    @Test
    void duplicateEmailThrows() {
        CustomerDto.CreateRequest req = new CustomerDto.CreateRequest();
        req.setFirstName("Bob");
        req.setLastName("Jones");
        req.setEmails(List.of("bob@example.com"));
        CustomerDto.PrivacySettingsDto ps = new CustomerDto.PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        req.setPrivacySettings(ps);
        service.create(CustomerDto.toEntity(req));

        CustomerDto.CreateRequest req2 = new CustomerDto.CreateRequest();
        req2.setFirstName("Bobby");
        req2.setLastName("Jones");
        req2.setEmails(List.of("bob@example.com"));
        req2.setPrivacySettings(ps);
        assertThrows(IllegalArgumentException.class, () -> service.create(CustomerDto.toEntity(req2)));
    }

    @Test
    void updateCustomer() {
        CustomerDto.CreateRequest req = new CustomerDto.CreateRequest();
        req.setFirstName("Carol");
        req.setLastName("White");
        req.setEmails(List.of("carol@example.com"));
        CustomerDto.PrivacySettingsDto ps = new CustomerDto.PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        req.setPrivacySettings(ps);
        Customer created = service.create(CustomerDto.toEntity(req));

        CustomerDto.CreateRequest upd = new CustomerDto.CreateRequest();
        upd.setFirstName("Caroline");
        upd.setLastName("White");
        upd.setEmails(List.of("caroline@example.com"));
        upd.setPrivacySettings(ps);
        Customer updated = service.update(created.getId(), CustomerDto.toEntity(upd));
        assertEquals("Caroline", updated.getFirstName());
    }

    @Test
    void deleteCustomer() {
        CustomerDto.CreateRequest req = new CustomerDto.CreateRequest();
        req.setFirstName("Dan");
        req.setLastName("Brown");
        req.setEmails(List.of("dan@example.com"));
        CustomerDto.PrivacySettingsDto ps = new CustomerDto.PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        req.setPrivacySettings(ps);
        Customer created = service.create(CustomerDto.toEntity(req));
        UUID id = created.getId();
        service.delete(id);
        assertThrows(Exception.class, () -> service.get(id));
    }
}
