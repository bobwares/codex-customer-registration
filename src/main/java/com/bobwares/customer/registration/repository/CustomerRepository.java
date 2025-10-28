package com.bobwares.customer.registration.repository;

import com.bobwares.customer.registration.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
