package com.bobwares.customer.registration.repository;

import com.bobwares.customer.registration.model.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
