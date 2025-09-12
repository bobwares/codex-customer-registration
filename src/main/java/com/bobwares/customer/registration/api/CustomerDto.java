/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: CreateRequest, UpdateRequest, Response
 * Description: DTOs for customer API requests and responses.
 */
package com.bobwares.customer.registration.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CustomerDto {
    public record PhoneNumberDto(@NotBlank String type, @NotBlank String number) {}

    public record PostalAddressDto(@NotBlank String line1, String line2, @NotBlank String city,
                                   @NotBlank String state, String postalCode, @NotBlank String country) {}

    public record PrivacySettingsDto(@NotNull Boolean marketingEmailsEnabled, @NotNull Boolean twoFactorEnabled) {}

    public record CreateRequest(
            @NotBlank String firstName,
            String middleName,
            @NotBlank String lastName,
            @NotEmpty @Email List<String> emails,
            @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers,
            @Valid PostalAddressDto address,
            @Valid @NotNull PrivacySettingsDto privacySettings) {}

    public record UpdateRequest(
            @NotBlank String firstName,
            String middleName,
            @NotBlank String lastName,
            @NotEmpty @Email List<String> emails,
            @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers,
            @Valid PostalAddressDto address,
            @Valid @NotNull PrivacySettingsDto privacySettings) {}

    public record Response(
            UUID id,
            String firstName,
            String middleName,
            String lastName,
            List<String> emails,
            List<PhoneNumberDto> phoneNumbers,
            PostalAddressDto address,
            PrivacySettingsDto privacySettings) {}
}
