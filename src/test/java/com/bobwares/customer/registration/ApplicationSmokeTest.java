/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: ApplicationSmokeTest
 * Description: Verifies that the Spring application context boots successfully.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationSmokeTest {

  @Test
  void contextLoads() {}
}
