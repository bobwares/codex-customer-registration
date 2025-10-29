/**
 * App: Customer Registration Package: com.bobwares.customer.registration.config File:
 * AppProperties.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com) Date:
 * 2025-10-29T19:49:40Z Exports: AppProperties Description: Exposes validated configuration
 * properties for high-level application metadata and runtime port values.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  @NotBlank private String name = "customer-registration";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;
}
