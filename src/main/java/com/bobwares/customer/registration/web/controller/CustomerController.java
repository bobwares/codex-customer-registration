package com.bobwares.customer.registration.web.controller;

import com.bobwares.customer.registration.mapper.CustomerMapper;
import com.bobwares.customer.registration.service.CustomerService;
import com.bobwares.customer.registration.web.dto.CustomerRequest;
import com.bobwares.customer.registration.web.dto.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        var created = customerService.create(customerMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/customers/" + created.getId()))
                .body(customerMapper.toResponse(created));
    }

    @GetMapping
    public List<CustomerResponse> list() {
        return customerService.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable UUID id) {
        return customerMapper.toResponse(customerService.findById(id));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody CustomerRequest request) {
        var updated = customerService.update(id, customerMapper.toEntity(request));
        return customerMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
