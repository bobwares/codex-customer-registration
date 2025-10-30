/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * AbstractIntegrationTest.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date:
 * 2025-10-30T06:53:03Z Exports: AbstractIntegrationTest Description: Provides a shared database
 * configuration for integration tests, preferring Testcontainers but falling back to H2 when Docker
 * is unavailable.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

  private static PostgreSQLContainer<?> postgres;
  private static boolean useH2;

  @BeforeAll
  static void startDatabase() {
    try {
      postgres = new PostgreSQLContainer<>("postgres:16").withDatabaseName("customer_registration");
      postgres.start();
    } catch (Throwable ex) {
      useH2 = true;
    }
  }

  @AfterAll
  static void stopDatabase() {
    if (postgres != null) {
      postgres.stop();
    }
  }

  @DynamicPropertySource
  static void registerDataSource(DynamicPropertyRegistry registry) {
    if (useH2 || postgres == null) {
      registry.add(
          "spring.datasource.url",
          () ->
              "jdbc:h2:mem:customer_registration;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
      registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
      registry.add("spring.datasource.username", () -> "sa");
      registry.add("spring.datasource.password", () -> "");
      registry.add("spring.liquibase.enabled", () -> false);
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
      registry.add("DATABASE_HOST", () -> "localhost");
      registry.add("DATABASE_PORT", () -> "5432");
      registry.add("DATABASE_NAME", () -> "customer_registration");
      registry.add("DATABASE_USERNAME", () -> "sa");
      registry.add("DATABASE_PASSWORD", () -> "");
    } else {
      registry.add("spring.datasource.url", postgres::getJdbcUrl);
      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
      registry.add("DATABASE_HOST", postgres::getHost);
      registry.add("DATABASE_PORT", () -> String.valueOf(postgres.getMappedPort(5432)));
      registry.add("DATABASE_NAME", postgres::getDatabaseName);
      registry.add("DATABASE_USERNAME", postgres::getUsername);
      registry.add("DATABASE_PASSWORD", postgres::getPassword);
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }
    registry.add("APP_NAME", () -> "customer-registration-test");
    registry.add("APP_PORT", () -> "0");
    registry.add("APP_DEFAULT_TAX_RATE", () -> "0.07");
    registry.add("APP_DEFAULT_SHIPPING_COST", () -> "4.99");
    registry.add("APP_SUPPORTED_CURRENCIES", () -> "USD,EUR");
  }
}
