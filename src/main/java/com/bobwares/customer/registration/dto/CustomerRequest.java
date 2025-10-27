package com.bobwares.customer.registration.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CustomerRequest(
        @NotBlank(message = "firstName is required") String firstName,
        String middleName,
        @NotBlank(message = "lastName is required") String lastName,
        @NotEmpty(message = "emails must contain at least one address") List<@Email String> emails,
        @NotEmpty(message = "phoneNumbers must contain at least one entry") List<@Valid PhoneNumberRequest> phoneNumbers,
        @Valid PostalAddressRequest address,
        @NotNull(message = "privacySettings is required") @Valid PrivacySettingsRequest privacySettings
) {
}
