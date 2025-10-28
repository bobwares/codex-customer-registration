package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import com.bobwares.customer.registration.mapper.CustomerMapper;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toEntity(request);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    public List<CustomerResponse> getCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    public CustomerResponse getCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateEntity(existing, request);
        Customer saved = customerRepository.save(existing);
        return customerMapper.toResponse(saved);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }
}
