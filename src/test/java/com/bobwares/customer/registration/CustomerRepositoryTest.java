/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepositoryTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: CustomerRepositoryTest
 * Description: Verifies persistence with Testcontainers-backed PostgreSQL.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link CustomerRepository} using a real PostgreSQL instance.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("APP_NAME", () -> "test-app");
        registry.add("APP_PORT", () -> "0");
    }

    @Autowired
    private CustomerRepository repository;

    /**
     * Persists and retrieves a customer entity.
     */
    @Test
    void saveAndFind() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("jane.doe@example.com");

        Customer saved = repository.save(customer);
        assertThat(repository.findById(saved.getId())).isPresent();
    }
}
