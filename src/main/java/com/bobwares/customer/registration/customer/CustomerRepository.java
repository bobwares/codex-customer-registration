/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: CustomerRepository
 * Description: Spring Data repository for managing customers.
 */
package com.bobwares.customer.registration.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
