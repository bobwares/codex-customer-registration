/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.controller
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: CustomerController
 * Description: REST controller exposing customer registration endpoints.
 */
package com.bobwares.customer.registration.controller;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * Handles HTTP requests for customer registration operations.
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    /**
     * Creates controller with the given service.
     *
     * @param service customer business service
     */
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    /**
     * Registers a new customer.
     *
     * @param customer request payload
     * @return created customer with location header
     */
    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
        Customer saved = service.save(customer);
        return ResponseEntity.created(URI.create("/api/v1/customers/" + saved.getId())).body(saved);
    }

    /**
     * Retrieves a customer by identifier.
     *
     * @param id customer identifier
     * @return customer or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
