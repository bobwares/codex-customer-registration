/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerPhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: CustomerPhoneNumber
 * Description: Entity representing a phone number associated with a customer.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_phone_number", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "number"}))
@Getter
@Setter
@NoArgsConstructor
public class CustomerPhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private Long phoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private PhoneType type;

    @NotBlank
    @Size(max = 15)
    @Column(name = "number", nullable = false, length = 15)
    private String number;
}
