/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: PhoneNumber
 * Description: Embeddable value object capturing a customer's phone number and its categorized type.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber {

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type", nullable = false, length = 16)
    private PhoneNumberType type;

    @Column(name = "phone_number", nullable = false, length = 32)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone numbers must follow E.164 format")
    @NotBlank(message = "Phone number is required")
    private String number;
}
