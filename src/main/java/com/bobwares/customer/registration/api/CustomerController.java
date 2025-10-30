/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD endpoints for customer profiles.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Customers", description = "CRUD operations for customer registrations")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  @Operation(summary = "Create a customer", description = "Registers a new customer profile")
  @ApiResponse(responseCode = "201", description = "Created")
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request,
      UriComponentsBuilder uriBuilder) {
    CustomerDto.Response response = customerService.create(request);
    URI location = uriBuilder.path("/api/customers/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get customer", description = "Retrieves a customer by id")
  @ApiResponse(responseCode = "200", description = "Customer found")
  @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(hidden = true)))
  public ResponseEntity<CustomerDto.Response> get(@PathVariable UUID id) {
    return ResponseEntity.ok(customerService.get(id));
  }

  @GetMapping
  @Operation(summary = "List customers", description = "Lists all customers")
  public ResponseEntity<List<CustomerDto.Response>> list() {
    return ResponseEntity.ok(customerService.list());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update customer", description = "Updates an existing customer profile")
  public ResponseEntity<CustomerDto.Response> update(
      @PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
    return ResponseEntity.ok(customerService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete customer", description = "Deletes a customer profile")
  @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(hidden = true)))
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
