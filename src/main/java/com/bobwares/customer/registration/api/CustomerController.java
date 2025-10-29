/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerController
 * Description: Exposes RESTful CRUD endpoints for managing customers with OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.api.CustomerDto.CreateRequest;
import com.bobwares.customer.registration.api.CustomerDto.Response;
import com.bobwares.customer.registration.api.CustomerDto.UpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Validated
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "CRUD operations for customer registrations")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create a customer")
  public ResponseEntity<Response> create(@Valid @RequestBody CreateRequest request) {
    Response response = service.create(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retrieve a customer by identifier")
  public ResponseEntity<Response> get(@PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping
  @Operation(summary = "List all customers")
  public ResponseEntity<List<Response>> list() {
    return ResponseEntity.ok(service.list());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a customer")
  public ResponseEntity<Response> update(
      @PathVariable UUID id, @Valid @RequestBody UpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
