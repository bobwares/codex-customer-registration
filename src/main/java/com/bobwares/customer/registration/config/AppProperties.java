/**
 * App: Customer Registration Package: com.bobwares.customer.registration.config File:
 * AppProperties.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: AppProperties Description: Declares validated configuration properties for the service's
 * application metadata and defaults.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  @NotBlank private String name = "customer-registration";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;

  @NotNull
  @DecimalMin("0.00")
  private BigDecimal defaultTaxRate = new BigDecimal("0.07");

  @NotNull
  @DecimalMin("0.00")
  private BigDecimal defaultShippingCost = new BigDecimal("4.99");

  @NotEmpty private List<@NotBlank String> supportedCurrencies = List.of("USD", "EUR");

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
