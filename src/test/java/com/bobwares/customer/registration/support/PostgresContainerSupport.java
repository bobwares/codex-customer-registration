/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.support
 * File: PostgresContainerSupport.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: PostgresContainerSupport
 * Description: Bootstraps a reusable PostgreSQL Testcontainers instance for integration tests.
 */
package com.bobwares.customer.registration.support;

import org.junit.jupiter.api.Assumptions;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
public abstract class PostgresContainerSupport {

  private static final PostgreSQLContainer<?> POSTGRES;
  private static final boolean DOCKER_AVAILABLE;

  static {
    PostgreSQLContainer<?> container = null;
    boolean available;
    try {
      DockerClientFactory.instance().client();
      available = true;
      container =
          new PostgreSQLContainer<>("postgres:16")
              .withDatabaseName("customer_registration")
              .withUsername("postgres")
              .withPassword("postgres");
      container.start();
    } catch (Throwable ex) {
      available = false;
    }
    DOCKER_AVAILABLE = available;
    POSTGRES = container;
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    Assumptions.assumeTrue(DOCKER_AVAILABLE, "Docker is required for database-backed tests");
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    registry.add("spring.liquibase.url", POSTGRES::getJdbcUrl);
    registry.add("spring.liquibase.user", POSTGRES::getUsername);
    registry.add("spring.liquibase.password", POSTGRES::getPassword);
    registry.add("APP_NAME", () -> "customer-registration-test");
    registry.add("APP_PORT", () -> "8080");
    registry.add("DATABASE_HOST", POSTGRES::getHost);
    registry.add("DATABASE_PORT", () -> String.valueOf(POSTGRES.getMappedPort(5432)));
    registry.add("DATABASE_NAME", POSTGRES::getDatabaseName);
    registry.add("DATABASE_USERNAME", POSTGRES::getUsername);
    registry.add("DATABASE_PASSWORD", POSTGRES::getPassword);
    registry.add("APP_DEFAULT_TAX_RATE", () -> "0");
    registry.add("APP_DEFAULT_SHIPPING_COST", () -> "0");
    registry.add("APP_SUPPORTED_CURRENCIES", () -> "USD");
  }
}
