/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: CustomerController
 * Description: REST controller providing CRUD operations for customers.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create customer")
    public ResponseEntity<CustomerDto.Response> create(@Valid @RequestBody CustomerDto.CreateRequest request) {
        Customer created = service.create(CustomerDto.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDto.fromEntity(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    public ResponseEntity<CustomerDto.Response> get(@PathVariable UUID id) {
        return ResponseEntity.ok(CustomerDto.fromEntity(service.get(id)));
    }

    @GetMapping
    @Operation(summary = "List customers")
    public ResponseEntity<List<CustomerDto.Response>> list() {
        return ResponseEntity.ok(service.list().stream().map(CustomerDto::fromEntity).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public ResponseEntity<CustomerDto.Response> update(@PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
        Customer updated = service.update(id, CustomerDto.toEntity(request));
        return ResponseEntity.ok(CustomerDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
