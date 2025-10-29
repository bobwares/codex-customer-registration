/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: AppProperties
 * Description: Binds validated application settings such as display name, port, and default commerce parameters.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

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
  @PositiveOrZero
  private BigDecimal defaultTaxRate = BigDecimal.ZERO;

  @NotNull
  @PositiveOrZero
  private BigDecimal defaultShippingCost = BigDecimal.ZERO;

  @NotEmpty
  private List<@NotBlank String> supportedCurrencies = new ArrayList<>(List.of("USD"));

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public BigDecimal getDefaultTaxRate() {
    return defaultTaxRate;
  }

  public void setDefaultTaxRate(BigDecimal defaultTaxRate) {
    this.defaultTaxRate = defaultTaxRate;
  }

  public BigDecimal getDefaultShippingCost() {
    return defaultShippingCost;
  }

  public void setDefaultShippingCost(BigDecimal defaultShippingCost) {
    this.defaultShippingCost = defaultShippingCost;
  }

  public List<String> getSupportedCurrencies() {
    return supportedCurrencies;
  }

  public void setSupportedCurrencies(List<String> supportedCurrencies) {
    this.supportedCurrencies = supportedCurrencies;
  }
}
