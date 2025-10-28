package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneNumberDto(
        @NotBlank(message = "phoneNumbers.type is required")
        @Pattern(regexp = "(?i)^(mobile|home|work|other)$", message = "phoneNumbers.type must be one of mobile, home, work, other")
        String type,
        @NotBlank(message = "phoneNumbers.number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "phoneNumbers.number must be in E.164 format")
        String number
) {
}
