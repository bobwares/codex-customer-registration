/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerController
 * Description: REST controller providing CRUD operations for customers.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "CRUD operations for customer registrations")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  @Operation(summary = "Create a customer")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Customer created", content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
      @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
  })
  public ResponseEntity<CustomerDto.Response> create(@Valid @RequestBody CustomerDto.CreateRequest request) {
    CustomerDto.Response response = customerService.create(request);
    return ResponseEntity.created(URI.create("/api/customers/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a customer by identifier")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  public ResponseEntity<CustomerDto.Response> get(@PathVariable UUID id) {
    return ResponseEntity.ok(customerService.get(id));
  }

  @GetMapping
  @Operation(summary = "List all customers")
  public ResponseEntity<List<CustomerDto.Response>> list() {
    return ResponseEntity.ok(customerService.list());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing customer")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer updated", content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  public ResponseEntity<CustomerDto.Response> update(@PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
    return ResponseEntity.ok(customerService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Customer deleted"),
      @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
