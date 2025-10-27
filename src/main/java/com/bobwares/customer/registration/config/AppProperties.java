/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: AppProperties
 * Description: Strongly typed configuration properties for application level settings.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  @Digits(integer = 5, fraction = 4)
  private BigDecimal defaultTaxRate = BigDecimal.ZERO;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = true)
  @Digits(integer = 10, fraction = 2)
  private BigDecimal defaultShippingCost = BigDecimal.ZERO;

  @NotEmpty
  private List<@NotBlank String> supportedCurrencies = List.of("USD");
}
