/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD operations for customer resources with OpenAPI annotations.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Manage customer registrations")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create customer", description = "Registers a new customer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
      })
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request, UriComponentsBuilder uriBuilder) {
    CustomerDto.Response response = service.create(request);
    URI location = uriBuilder.path("/api/customers/{id}").buildAndExpand(response.getId()).toUri();
    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get customer", description = "Retrieves a customer by identifier")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
      })
  public ResponseEntity<CustomerDto.Response> get(
      @Parameter(description = "Customer identifier") @PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping
  @Operation(summary = "List customers", description = "Lists all customers")
  public ResponseEntity<List<CustomerDto.Response>> list() {
    return ResponseEntity.ok(service.list());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update customer", description = "Updates an existing customer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customer updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
        @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
      })
  public ResponseEntity<CustomerDto.Response> update(
      @Parameter(description = "Customer identifier") @PathVariable UUID id,
      @Valid @RequestBody CustomerDto.UpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete customer", description = "Deletes a customer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Deleted"),
        @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
      })
  public ResponseEntity<Void> delete(@Parameter(description = "Customer identifier") @PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
