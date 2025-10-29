/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * CustomerRepository.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T19:49:40Z Exports: CustomerRepository Description: Spring Data repository for
 * interacting with persisted customers and executing email-based lookups.
 */
package com.bobwares.customer.registration;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
  boolean existsByEmailsEmailIgnoreCase(String email);

  boolean existsByIdNotAndEmailsEmailIgnoreCase(UUID id, String email);

  Optional<Customer> findByEmailsEmailIgnoreCase(String email);
}
