/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-28T15:01:20Z
 * Exports: MetaController
 * Description: Exposes service metadata endpoints backed by validated application properties.
 */
package com.bobwares.customer.registration.web;

import com.bobwares.customer.registration.config.AppProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meta")
public class MetaController {

  private final AppProperties props;

  public MetaController(AppProperties props) {
    this.props = props;
  }

  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of(
        "app",
        props.getName(),
        "port",
        props.getPort(),
        "defaultTaxRate",
        props.getDefaultTaxRate(),
        "defaultShippingCost",
        props.getDefaultShippingCost(),
        "supportedCurrencies",
        props.getSupportedCurrencies());
  }
}
