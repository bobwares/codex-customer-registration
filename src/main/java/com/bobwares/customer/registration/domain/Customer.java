/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.domain
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-09-12T19:54:27Z
 * Exports: Customer
 * Description: JPA entity representing a customer profile.
 */
package com.bobwares.customer.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;

/**
 * Represents a customer record persisted to the database.
 */
@Data
@Entity
@Table(name = "customers")
public class Customer {

    /** Identifier for the customer. */
    @Id
    private UUID id;

    /** Customer given name. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Customer middle name. */
    @Column(name = "middle_name")
    private String middleName;

    /** Customer family name. */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** Primary email address. */
    @Column(nullable = false)
    private String email;
}
