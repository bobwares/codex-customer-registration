/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.repository
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerRepository
 * Description: Spring Data repository with convenience lookups for customer aggregates.
 */
package com.bobwares.customer.registration.customer.repository;

import com.bobwares.customer.registration.customer.model.Customer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @EntityGraph(attributePaths = {"address", "privacySettings", "emails", "phoneNumbers"})
    Optional<Customer> findById(UUID id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Customer c JOIN c.emails e "
            + "WHERE LOWER(e.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Customer c JOIN c.emails e "
            + "WHERE LOWER(e.email) = LOWER(:email) AND c.id <> :customerId")
    boolean existsByEmailIgnoreCaseForOtherCustomer(@Param("email") String email,
                                                    @Param("customerId") UUID customerId);
}
