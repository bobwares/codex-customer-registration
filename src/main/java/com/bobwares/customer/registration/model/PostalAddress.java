/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: PostalAddress
 * Description: Embeddable structure storing postal address details for a customer profile.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostalAddress {

    @Column(name = "address_line1", nullable = false, length = 128)
    @NotBlank(message = "Address line1 is required")
    private String line1;

    @Column(name = "address_line2", length = 128)
    private String line2;

    @Column(name = "address_city", nullable = false, length = 64)
    @NotBlank(message = "City is required")
    private String city;

    @Column(name = "address_state", nullable = false, length = 64)
    @NotBlank(message = "State is required")
    private String state;

    @Column(name = "address_postal_code", nullable = false, length = 32)
    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @Column(name = "address_country", nullable = false, length = 2)
    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country must be a 2-letter ISO code")
    private String country;
}
