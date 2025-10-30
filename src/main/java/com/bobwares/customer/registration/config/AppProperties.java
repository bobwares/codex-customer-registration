/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: AppProperties
 * Description: Provides validated configuration properties loaded from the application configuration namespace.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @NotBlank private String name = "customer-registration";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal defaultTaxRate = BigDecimal.ZERO;

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal defaultShippingCost = BigDecimal.ZERO;

  @Size(min = 1)
  private List<@NotBlank String> supportedCurrencies = new ArrayList<>(List.of("USD"));
}
