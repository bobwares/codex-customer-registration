/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: PhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: PhoneNumber
 * Description: Embeddable representing a phone number for a customer.
 */
package com.bobwares.customer.registration.customer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Embeddable
public class PhoneNumber {

    @NotBlank
    private String type;

    @NotBlank
    private String number;
}
