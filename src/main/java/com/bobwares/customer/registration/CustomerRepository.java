/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerRepository
 * Description: Spring Data repository exposing CRUD and convenience lookups for customers.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  @Override
  @EntityGraph(attributePaths = {"emails", "phoneNumbers", "address", "privacySettings"})
  Optional<Customer> findById(UUID id);

  @Override
  @EntityGraph(attributePaths = {"emails", "phoneNumbers", "address", "privacySettings"})
  List<Customer> findAll();

  boolean existsByEmailsEmailIgnoreCase(String email);
}
