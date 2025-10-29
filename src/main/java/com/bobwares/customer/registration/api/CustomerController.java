/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerController
 * Description: Exposes RESTful CRUD endpoints for managing customers with OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.api.CustomerDto.CreateRequest;
import com.bobwares.customer.registration.api.CustomerDto.Response;
import com.bobwares.customer.registration.api.CustomerDto.UpdateRequest;
import com.bobwares.customer.registration.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/customers")
@Validated
@Tag(name = "Customers", description = "Customer registration management endpoints")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create a new customer")
  public ResponseEntity<Response> create(@Valid @RequestBody CreateRequest request) {
    Response response = service.create(request);
    return ResponseEntity.created(URI.create("/api/customers/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retrieve a customer by identifier")
  public Response get(@Parameter(description = "Customer identifier") @PathVariable UUID id) {
    return service.get(id);
  }

  @GetMapping
  @Operation(summary = "List customers ordered by name")
  public List<Response> list() {
    return service.list();
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing customer")
  public Response update(
      @Parameter(description = "Customer identifier") @PathVariable UUID id,
      @Valid @RequestBody UpdateRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Customer identifier") @PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
