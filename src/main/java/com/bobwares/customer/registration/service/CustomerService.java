package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import com.bobwares.customer.registration.mapper.CustomerMapper;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CustomerResponse create(CustomerRequest request) {
        Customer customer = mapper.toNewEntity(request);
        Customer saved = repository.save(customer);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse get(UUID id) {
        return mapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> list() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer existing = findById(id);
        Customer updated = mapper.toExistingEntity(request, existing);
        return mapper.toResponse(repository.save(updated));
    }

    public void delete(UUID id) {
        Customer existing = findById(id);
        repository.delete(existing);
    }

    private Customer findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
