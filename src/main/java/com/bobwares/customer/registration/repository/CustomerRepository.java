/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.repository
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerRepository
 * Description: Spring Data repository exposing CRUD access to persisted customer entities.
 */
package com.bobwares.customer.registration.repository;

import com.bobwares.customer.registration.model.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
