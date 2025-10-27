package com.bobwares.customer.registration.dto;

public record PrivacySettingsResponse(
        boolean marketingEmailsEnabled,
        boolean twoFactorEnabled
) {
}
