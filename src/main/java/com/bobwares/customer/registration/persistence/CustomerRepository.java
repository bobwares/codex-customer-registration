/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.persistence
 * File: CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerRepository
 * Description: Spring Data repository with convenience lookups for enforcing customer uniqueness constraints.
 */
package com.bobwares.customer.registration.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmailsValueIgnoreCase(String email);

  boolean existsByPhoneNumbersNumber(String number);

  boolean existsByEmailsValueIgnoreCaseAndIdNot(String email, UUID id);

  boolean existsByPhoneNumbersNumberAndIdNot(String number, UUID id);
}
