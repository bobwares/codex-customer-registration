/**
 * App: Customer Registration Package: com.bobwares.customer.registration.repository File:
 * CustomerRepository.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: CustomerRepository Description: Provides database access utilities for managing customer
 * aggregates.
 */
package com.bobwares.customer.registration.repository;

import com.bobwares.customer.registration.model.Customer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmailsEmailIgnoreCase(String email);

  Optional<Customer> findByEmailsEmailIgnoreCase(String email);
}
