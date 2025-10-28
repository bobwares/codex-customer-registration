/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.service
 * File: CustomerService.java
 * Version: 0.1.1
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerService
 * Description: Application service orchestrating customer CRUD interactions between DTOs and persistence layer.
 */
package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import com.bobwares.customer.registration.support.CustomerMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
            .map(CustomerMapper::toResponse)
            .collect(Collectors.toList());
    }

    public CustomerResponse findById(UUID id) {
        return CustomerMapper.toResponse(getById(id));
    }

    public CustomerResponse create(CustomerRequest request) {
        Customer customer = mapSafely(request);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toResponse(saved);
    }

    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer existing = getById(id);
        applySafely(existing, request);
        Customer updated = customerRepository.save(existing);
        return CustomerMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        Customer existing = getById(id);
        customerRepository.delete(existing);
    }

    private Customer getById(UUID id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private Customer mapSafely(CustomerRequest request) {
        try {
            return CustomerMapper.toEntity(request);
        } catch (IllegalArgumentException exception) {
            throw normalizePhoneTypeFailure(exception);
        }
    }

    private void applySafely(Customer existing, CustomerRequest request) {
        try {
            CustomerMapper.updateEntity(existing, request);
        } catch (IllegalArgumentException exception) {
            throw normalizePhoneTypeFailure(exception);
        }
    }

    private IllegalArgumentException normalizePhoneTypeFailure(IllegalArgumentException exception) {
        String message = exception.getMessage();
        if (message != null && message.contains("No enum constant")) {
            return new IllegalArgumentException("Unsupported phone number type supplied. Accepted values: "
                + java.util.Arrays.stream(com.bobwares.customer.registration.model.PhoneNumberType.values())
                .map(value -> value.name().toLowerCase(Locale.US))
                .collect(Collectors.joining(", ")));
        }
        return exception;
    }
}
