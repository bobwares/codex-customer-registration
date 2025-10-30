/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: CustomerRepository
 * Description: Spring Data repository exposing persistence operations for customer aggregates.
 */
package com.bobwares.customer.registration;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmails(String email);

  Optional<Customer> findByEmails(String email);
}
