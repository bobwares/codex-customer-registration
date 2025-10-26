/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD APIs for managing customer registrations.
 */
package com.bobwares.customer.registration.customer.api;

import com.bobwares.customer.registration.customer.model.Customer;
import com.bobwares.customer.registration.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Customers", description = "Customer registration lifecycle operations")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created",
                    content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    public ResponseEntity<CustomerDto.Response> create(@Valid @RequestBody CustomerDto.CreateRequest request) {
        Customer created = service.create(CustomerMapper.toEntity(request));
        CustomerDto.Response response = CustomerMapper.toResponse(created);
        return ResponseEntity.created(URI.create("/api/customers/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer located",
                    content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public CustomerDto.Response get(@Parameter(description = "Customer identifier") @PathVariable UUID id) {
        return CustomerMapper.toResponse(service.get(id));
    }

    @GetMapping
    @Operation(summary = "List customers")
    public List<CustomerDto.Response> list() {
        return service.list().stream().map(CustomerMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated",
                    content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public CustomerDto.Response update(@PathVariable UUID id,
                                        @Valid @RequestBody CustomerDto.UpdateRequest request) {
        if (!id.equals(request.id())) {
            throw new IllegalArgumentException("Path id must match payload id");
        }
        Customer changes = CustomerMapper.toEntity(request);
        return CustomerMapper.toResponse(service.update(id, changes));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
