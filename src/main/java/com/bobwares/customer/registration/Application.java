/**
 * App: Customer Registration Package: com.bobwares.customer.registration File: Application.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com) Date: 2025-10-29T19:49:40Z
 * Exports: Application Description: Boots the Spring Boot application context and enables
 * configuration properties scanning for the service.
 */
package com.bobwares.customer.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.bobwares.customer.registration.config")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
