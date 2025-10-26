/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: PostalAddress
 * Description: Entity representing a postal address.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postal_address")
@Getter
@Setter
@NoArgsConstructor
public class PostalAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "line1", nullable = false, length = 255)
    private String line1;

    @Size(max = 255)
    @Column(name = "line2", length = 255)
    private String line2;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 50)
    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "country", nullable = false, length = 2)
    private String country;
}
