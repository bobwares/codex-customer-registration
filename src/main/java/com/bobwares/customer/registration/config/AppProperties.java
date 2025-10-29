/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: AppProperties
 * Description: Strongly typed configuration properties for the application metadata and defaults exposed via the meta endpoint.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
  @DecimalMin(value = "0.0", inclusive = true)
  @DecimalMax(value = "100.0", inclusive = true)
  private BigDecimal defaultTaxRate = BigDecimal.ZERO;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = true)
  private BigDecimal defaultShippingCost = BigDecimal.ZERO;

  @NotEmpty
  @Size(min = 1)
  private List<@NotBlank String> supportedCurrencies = List.of("USD");
}
