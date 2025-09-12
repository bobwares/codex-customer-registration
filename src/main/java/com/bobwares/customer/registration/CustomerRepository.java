/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: CustomerRepository
 * Description: Spring Data repository for Customer entities.
 */
package com.bobwares.customer.registration;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmailsEmail(String email);
    Optional<Customer> findByEmailsEmail(String email);
}
