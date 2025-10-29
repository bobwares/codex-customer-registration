/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerRepository
 * Description: Spring Data repository providing persistence operations and uniqueness lookups for customers.
 */
package com.bobwares.customer.registration;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByPrimaryEmailIgnoreCase(String primaryEmail);

  Optional<Customer> findByPrimaryEmailIgnoreCase(String primaryEmail);

  @Query("select case when count(c) > 0 then true else false end from Customer c join c.emails e where lower(e) = lower(:email)")
  boolean existsByEmail(@Param("email") String email);

  @Query("select c from Customer c join c.emails e where lower(e) = lower(:email)")
  Optional<Customer> findByEmail(@Param("email") String email);
}
