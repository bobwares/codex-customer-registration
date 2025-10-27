/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: ApplicationSmokeTest
 * Description: Ensures that the Spring Boot application context loads successfully.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
        "APP_NAME=customer-registration-test",
        "APP_PORT=8080",
        "APP_DEFAULT_TAX_RATE=0.0000",
        "APP_DEFAULT_SHIPPING_COST=0.00",
        "APP_SUPPORTED_CURRENCIES=USD,EUR",
        "spring.datasource.url=jdbc:h2:mem:smoke;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none"
    })
class ApplicationSmokeTest {

  @Test
  void contextLoads() {
  }
}
