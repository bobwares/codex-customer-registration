/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.repository
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: CustomerRepository
 * Description: Spring Data JPA repository for Customer entities.
 */
package com.bobwares.customer.registration.repository;

import com.bobwares.customer.registration.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Provides CRUD operations for {@link Customer} instances.
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
