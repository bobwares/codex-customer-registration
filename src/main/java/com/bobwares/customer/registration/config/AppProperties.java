/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: AppProperties
 * Description: Defines validated application configuration properties for core metadata and port settings.
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

  @NotBlank
  private String name = "backend";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;
}
