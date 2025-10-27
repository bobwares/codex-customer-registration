/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerRepository
 * Description: Spring Data repository providing persistence utilities for Customer aggregates.
 */
package com.bobwares.customer.registration;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  @Query(
      "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c JOIN c.emails e WHERE LOWER(e) = LOWER(:email)")
  boolean existsByEmail(@Param("email") String email);

  @Query(
      "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c JOIN c.emails e "
          + "WHERE LOWER(e) = LOWER(:email) AND (:excludedId IS NULL OR c.id <> :excludedId)")
  boolean existsByEmailExcludingId(
      @Param("email") String email, @Param("excludedId") UUID excludedId);
}
