package com.bobwares.customer.registration.dto;

public record PostalAddressResponse(
        String line1,
        String line2,
        String city,
        String state,
        String postalCode,
        String country
) {
}
