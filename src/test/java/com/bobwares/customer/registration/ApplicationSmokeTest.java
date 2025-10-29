/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * ApplicationSmokeTest.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T19:49:40Z Exports: ApplicationSmokeTest Description: Verifies that the Spring
 * application context loads using the generated configuration baseline.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(
    properties = {
      "APP_NAME=customer-registration",
      "APP_PORT=8080",
      "DATABASE_HOST=localhost",
      "DATABASE_PORT=5432",
      "DATABASE_NAME=testdb",
      "DATABASE_USERNAME=test",
      "DATABASE_PASSWORD=test",
      "APP_DEFAULT_TAX_RATE=0.00",
      "APP_DEFAULT_SHIPPING_COST=0.00",
      "APP_SUPPORTED_CURRENCIES=USD",
      "spring.autoconfigure.exclude="
          + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
          + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
          + "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration"
    })
class ApplicationSmokeTest {

  @MockBean private CustomerRepository customerRepository;

  @Test
  void contextLoads() {}
}
