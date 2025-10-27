/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: MetaController
 * Description: Exposes diagnostic metadata endpoints for the Customer Registration service.
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

  private final AppProperties appProperties;

  public MetaController(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of("app", appProperties.getName(), "port", appProperties.getPort());
  }
}
