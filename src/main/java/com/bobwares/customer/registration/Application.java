/**
 * App: Customer Registration Package: com.bobwares.customer.registration File: Application.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z Exports: Application
 * Description: Bootstraps the Spring Boot application and triggers configuration property scanning.
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
