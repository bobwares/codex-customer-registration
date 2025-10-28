package com.bobwares.customer.registration.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CustomerRequest(
        @NotBlank(message = "firstName is required") String firstName,
        String middleName,
        @NotBlank(message = "lastName is required") String lastName,
        @NotEmpty(message = "emails must not be empty") List<@jakarta.validation.constraints.Email(message = "must be a valid email") String> emails,
        @NotNull(message = "phoneNumbers is required") @Size(min = 1, message = "phoneNumbers must contain at least one value") List<@Valid PhoneNumberDto> phoneNumbers,
        @NotNull(message = "address is required") @Valid PostalAddressDto address,
        @NotNull(message = "privacySettings is required") @Valid PrivacySettingsDto privacySettings
) {
}
