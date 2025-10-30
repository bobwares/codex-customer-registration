/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: ApplicationSmokeTest
 * Description: Verifies that the Spring application context loads successfully for smoke coverage.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.support.PostgresContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationSmokeTest extends PostgresContainerSupport {

  @Test
  void contextLoads() {}
}
