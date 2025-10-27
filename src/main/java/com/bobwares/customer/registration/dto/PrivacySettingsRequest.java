package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotNull;

public record PrivacySettingsRequest(
        @NotNull(message = "marketingEmailsEnabled is required") Boolean marketingEmailsEnabled,
        @NotNull(message = "twoFactorEnabled is required") Boolean twoFactorEnabled
) {
}
