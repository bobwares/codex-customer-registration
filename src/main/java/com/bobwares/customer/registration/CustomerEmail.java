/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerEmail.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: CustomerEmail
 * Description: Entity representing an email address associated with a customer.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_email", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "email"}))
@Getter
@Setter
@NoArgsConstructor
public class CustomerEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;
}
