/**
 * App: Customer Registration Package: com.bobwares.customer.registration.api File:
 * CustomerController.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: CustomerController Description: Exposes RESTful CRUD endpoints for customer resources
 * with OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Customers", description = "Manage customer registrations")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @Operation(
      summary = "Create a customer",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer created",
            content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "Validation error")
      })
  @PostMapping
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request) {
    CustomerDto.Response response = service.create(request);
    return ResponseEntity.created(URI.create("/api/customers/" + response.getId())).body(response);
  }

  @Operation(
      summary = "Retrieve a customer",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer found",
            content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto.Response> get(
      @Parameter(description = "Customer identifier") @PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @Operation(
      summary = "List customers",
      responses =
          @ApiResponse(
              responseCode = "200",
              description = "Customers listed",
              content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))))
  @GetMapping
  public ResponseEntity<List<CustomerDto.Response>> list() {
    return ResponseEntity.ok(service.list());
  }

  @Operation(
      summary = "Update a customer",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer updated",
            content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "400", description = "Validation error")
      })
  @PutMapping("/{id}")
  public ResponseEntity<CustomerDto.Response> update(
      @Parameter(description = "Customer identifier") @PathVariable UUID id,
      @Valid @RequestBody CustomerDto.UpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @Operation(
      summary = "Delete a customer",
      responses = @ApiResponse(responseCode = "204", description = "Customer deleted"))
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Customer identifier") @PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
