/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: Customer
 * Description: Root Customer entity with relationships to addresses, privacy settings, emails, and phone numbers.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "customer_id")
    private UUID id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "middle_name", length = 255)
    private String middleName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private PostalAddress address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "privacy_settings_id", nullable = false)
    private PrivacySettings privacySettings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerEmail> emails = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerPhoneNumber> phoneNumbers = new ArrayList<>();
}
