/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD endpoints for managing customer registrations.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer registration API")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create a customer")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
    @ApiResponse(responseCode = "400", description = "Validation failure")
  })
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request) {
    Customer created = service.create(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
    return ResponseEntity.created(location).body(CustomerDto.Response.from(created));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a customer by identifier")
  @ApiResponse(
      responseCode = "200",
      description = "Customer",
      content = @Content(schema = @Schema(implementation = CustomerDto.Response.class)))
  public CustomerDto.Response get(@PathVariable UUID id) {
    return CustomerDto.Response.from(service.get(id));
  }

  @GetMapping
  @Operation(summary = "List customers")
  @ApiResponse(
      responseCode = "200",
      description = "Customers",
      content = @Content(schema = @Schema(implementation = CustomerDto.Response.class)))
  public List<CustomerDto.Response> list() {
    return service.list().stream().map(CustomerDto.Response::from).toList();
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a customer")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Updated"),
    @ApiResponse(responseCode = "404", description = "Not found")
  })
  public CustomerDto.Response update(
      @PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
    return CustomerDto.Response.from(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Deleted"),
    @ApiResponse(responseCode = "404", description = "Not found")
  })
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
