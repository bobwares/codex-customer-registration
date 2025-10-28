package com.bobwares.customer.registration.dto;

import jakarta.validation.constraints.NotNull;

public record PrivacySettingsDto(
        @NotNull(message = "privacySettings.marketingEmailsEnabled is required") Boolean marketingEmailsEnabled,
        @NotNull(message = "privacySettings.twoFactorEnabled is required") Boolean twoFactorEnabled
) {
}
