package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostalAddressDto(
        @NotBlank(message = "address.line1 is required") String line1,
        String line2,
        @NotBlank(message = "address.city is required") String city,
        @NotBlank(message = "address.state is required") String state,
        @NotBlank(message = "address.postalCode is required") String postalCode,
        @NotBlank(message = "address.country is required")
        @Size(min = 2, max = 2, message = "address.country must be an ISO 3166-1 alpha-2 code") String country
) {
}
