/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-28T15:01:20Z
 * Exports: AppProperties
 * Description: Provides validated configuration properties for application identity, networking, and defaults.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  @NotBlank
  private String name = "customer-registration";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;

  @NotNull
  private BigDecimal defaultTaxRate = new BigDecimal("0.00");

  @NotNull
  private BigDecimal defaultShippingCost = new BigDecimal("0.00");

  @NotEmpty
  private List<String> supportedCurrencies = new ArrayList<>(List.of("USD"));
}
