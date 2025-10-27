/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerRepository
 * Description: Spring Data repository for accessing Customer aggregates.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.model.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmailsEmailIgnoreCase(String email);

  boolean existsByIdNotAndEmailsEmailIgnoreCase(UUID id, String email);
}
