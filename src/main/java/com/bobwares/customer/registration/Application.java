/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Application.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: Application
 * Description: Bootstraps the Customer Registration Spring Boot application and scans configuration properties.
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
