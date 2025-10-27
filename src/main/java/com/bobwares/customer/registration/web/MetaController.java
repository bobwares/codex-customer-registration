/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: MetaController
 * Description: Provides lightweight metadata endpoints exposing environment information.
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
    return Map.of("app", properties.getName(), "port", properties.getPort());
  }
}
