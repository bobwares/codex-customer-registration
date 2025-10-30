/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerRepository
 * Description: Spring Data repository exposing persistence utilities for customer aggregates with email uniqueness helpers.
 */
package com.bobwares.customer.registration;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  @Query(
      "select count(c) > 0 from Customer c join c.emails email "
          + "where lower(email) = lower(:email) "
          + "and (:excludeId is null or c.id <> :excludeId)")
  boolean existsByEmailIgnoreCase(
      @Param("email") String email, @Param("excludeId") UUID excludeId);
}
