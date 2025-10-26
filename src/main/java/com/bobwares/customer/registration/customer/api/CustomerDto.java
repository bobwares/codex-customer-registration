/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerDto
 * Description: DTO definitions for customer create, update, and response payloads with validation constraints.
 */
package com.bobwares.customer.registration.customer.api;

import com.bobwares.customer.registration.customer.model.PhoneType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public final class CustomerDto {

    private CustomerDto() {
    }

    public record CreateRequest(
            @NotBlank @Size(max = 100) String firstName,
            @Size(max = 100) String middleName,
            @NotBlank @Size(max = 100) String lastName,
            @NotEmpty List<@Email @Size(max = 320) String> emails,
            @Valid Address address,
            @Valid @NotNull PrivacySettings privacySettings,
            @Valid List<PhoneNumber> phoneNumbers
    ) {
    }

    public record UpdateRequest(
            @NotNull UUID id,
            @NotBlank @Size(max = 100) String firstName,
            @Size(max = 100) String middleName,
            @NotBlank @Size(max = 100) String lastName,
            @NotEmpty List<@Email @Size(max = 320) String> emails,
            @Valid Address address,
            @Valid @NotNull PrivacySettings privacySettings,
            @Valid List<PhoneNumber> phoneNumbers
    ) {
    }

    public record Response(
            UUID id,
            String firstName,
            String middleName,
            String lastName,
            List<String> emails,
            Address address,
            PrivacySettings privacySettings,
            List<PhoneNumber> phoneNumbers
    ) {
    }

    public record Address(
            @NotBlank @Size(max = 255) String line1,
            @Size(max = 255) String line2,
            @NotBlank @Size(max = 100) String city,
            @NotBlank @Size(max = 100) String state,
            @NotBlank @Size(max = 20) String postalCode,
            @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String country
    ) {
    }

    public record PhoneNumber(
            @NotNull PhoneType type,
            @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") String number
    ) {
    }

    public record PrivacySettings(
            boolean marketingEmailsEnabled,
            boolean twoFactorEnabled
    ) {
    }
}
