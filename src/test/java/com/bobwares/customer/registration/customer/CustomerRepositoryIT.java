/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: CustomerRepositoryIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: CustomerRepositoryIT
 * Description: Integration test verifying persistence operations for Customer.
 */
package com.bobwares.customer.registration.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class CustomerRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerRepository repository;

    @Test
    void saveAndFind() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        PrivacySettings settings = new PrivacySettings();
        settings.setMarketingEmailsEnabled(true);
        settings.setTwoFactorEnabled(false);
        customer.setPrivacySettings(settings);
        customer.getEmails().add("john@example.com");

        Customer saved = repository.save(customer);
        Optional<Customer> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
    }
}
