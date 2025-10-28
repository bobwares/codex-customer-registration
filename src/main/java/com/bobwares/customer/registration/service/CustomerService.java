package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
    }

    public Customer update(UUID id, Customer update) {
        Customer existing = findById(id);
        existing.setFirstName(update.getFirstName());
        existing.setMiddleName(update.getMiddleName());
        existing.setLastName(update.getLastName());
        existing.setEmails(update.getEmails());
        existing.setPhoneNumbers(update.getPhoneNumbers());
        existing.setAddress(update.getAddress());
        existing.setPrivacySettings(update.getPrivacySettings());
        return customerRepository.save(existing);
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer %s not found".formatted(id));
        }
        customerRepository.deleteById(id);
    }
}
