/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: ApplicationSmokeTest
 * Description: Ensures the Spring context loads successfully for the application entry point.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:smoketest;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.liquibase.enabled=false",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
    })
class ApplicationSmokeTest {

  @Test
  void contextLoads() {}
}
