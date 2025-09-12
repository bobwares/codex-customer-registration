/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: RegistrationApplication.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: RegistrationApplication
 * Description: Bootstraps the Spring Boot application.
 */
package com.bobwares.customer.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstraps the Spring application.
 */
@SpringBootApplication
public class RegistrationApplication {

    /**
     * Runs the application.
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RegistrationApplication.class, args);
    }
}
