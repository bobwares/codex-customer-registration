package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotBlank;

public record PostalAddressRequest(
        @NotBlank(message = "line1 is required") String line1,
        String line2,
        @NotBlank(message = "city is required") String city,
        @NotBlank(message = "state is required") String state,
        @NotBlank(message = "postalCode is required") String postalCode,
        @NotBlank(message = "country is required") String country
) {
}
