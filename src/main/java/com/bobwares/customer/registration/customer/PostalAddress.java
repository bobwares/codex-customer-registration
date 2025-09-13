/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: PostalAddress
 * Description: Embeddable representing a postal address for a customer.
 */
package com.bobwares.customer.registration.customer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Embeddable
public class PostalAddress {

    @NotBlank
    private String line1;

    private String line2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    private String postalCode;

    @NotBlank
    private String country;
}
