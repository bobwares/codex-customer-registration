/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: MetaController
 * Description: Provides metadata endpoints for verifying runtime configuration values.
 */
package com.bobwares.customer.registration.web;

import com.bobwares.customer.registration.config.AppProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meta")
@Tag(name = "Meta", description = "Service metadata endpoints")
public class MetaController {

  private final AppProperties props;

  public MetaController(AppProperties props) {
    this.props = props;
  }

  @GetMapping("/env")
  @Operation(summary = "Expose key runtime configuration values")
  public Map<String, Object> env() {
    return Map.of(
        "app", props.getName(),
        "port", props.getPort(),
        "defaultTaxRate", props.getDefaultTaxRate(),
        "defaultShippingCost", props.getDefaultShippingCost(),
        "supportedCurrencies", props.getSupportedCurrencies());
  }
}
