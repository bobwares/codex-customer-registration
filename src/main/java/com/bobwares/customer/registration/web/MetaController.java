/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: MetaController
 * Description: Exposes metadata endpoints for inspecting the running configuration of the service.
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

  private final AppProperties properties;

  public MetaController(AppProperties properties) {
    this.properties = properties;
  }

  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of(
        "app", properties.getName(),
        "port", properties.getPort(),
        "supportedCurrencies", properties.getSupportedCurrencies());
  }
}
