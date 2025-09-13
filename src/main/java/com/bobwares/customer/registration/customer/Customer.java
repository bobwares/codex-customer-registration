/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: Customer
 * Description: JPA entity representing a customer profile.
 */
package com.bobwares.customer.registration.customer;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @ElementCollection
    @CollectionTable(name = "customer_emails", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "email")
    private Set<@Email String> emails = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "customer_phone_numbers", joinColumns = @JoinColumn(name = "customer_id"))
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    @Embedded
    private PostalAddress address;

    @Embedded
    private PrivacySettings privacySettings;
}
