/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.dto
 * File: CustomerRequest.java
 * Version: 0.1.1
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerRequest, AddressRequest, PhoneNumberRequest, PrivacySettingsRequest
 * Description: Request payload definition for creating or updating customer records through the REST API.
 */
package com.bobwares.customer.registration.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "At least one email is required")
    @Size(min = 1, message = "At least one email is required")
    private Set<@Email(message = "Email must be valid") String> emails = new LinkedHashSet<>();

    @NotEmpty(message = "At least one phone number is required")
    @Size(min = 1, message = "At least one phone number is required")
    private Set<@Valid PhoneNumberRequest> phoneNumbers = new LinkedHashSet<>();

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;

    @Valid
    @NotNull(message = "Privacy settings are required")
    private PrivacySettingsRequest privacySettings;

    @Getter
    @Setter
    public static class AddressRequest {

        @NotBlank(message = "Address line1 is required")
        private String line1;

        private String line2;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "State is required")
        private String state;

        @NotBlank(message = "Postal code is required")
        private String postalCode;

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 2, message = "Country must be a 2-letter ISO code")
        private String country;
    }

    @Getter
    @Setter
    public static class PhoneNumberRequest {

        @NotBlank(message = "Phone number type is required")
        private String type;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone numbers must follow E.164 format")
        private String number;
    }

    @Getter
    @Setter
    public static class PrivacySettingsRequest {

        private boolean marketingEmailsEnabled;

        private boolean twoFactorEnabled;
    }
}
