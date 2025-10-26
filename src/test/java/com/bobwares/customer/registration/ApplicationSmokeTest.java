/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: ApplicationSmokeTest
 * Description: Verifies that the Spring application context loads successfully.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationSmokeTest {

    @Test
    void contextLoads() {
    }
}
