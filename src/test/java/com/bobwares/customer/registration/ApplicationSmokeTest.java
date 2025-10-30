/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * ApplicationSmokeTest.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date:
 * 2025-10-30T06:53:03Z Exports: ApplicationSmokeTest Description: Verifies that the Spring Boot
 * application context loads successfully.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationSmokeTest extends AbstractIntegrationTest {

  @Test
  void contextLoads() {}
}
