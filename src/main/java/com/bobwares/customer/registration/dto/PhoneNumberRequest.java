package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneNumberRequest(
        @NotBlank(message = "type is required")
        @Pattern(regexp = "mobile|home|work|other", message = "type must be one of mobile, home, work, other")
        String type,
        @NotBlank(message = "number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "number must be in E.164 format")
        String number
) {
}
