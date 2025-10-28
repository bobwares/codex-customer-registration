package com.bobwares.customer.registration.web.dto;

import jakarta.validation.constraints.NotNull;

public class PrivacySettingsDto {

    @NotNull
    private Boolean marketingEmailsEnabled;

    @NotNull
    private Boolean twoFactorEnabled;

    public PrivacySettingsDto() {
    }

    public PrivacySettingsDto(Boolean marketingEmailsEnabled, Boolean twoFactorEnabled) {
        this.marketingEmailsEnabled = marketingEmailsEnabled;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public Boolean getMarketingEmailsEnabled() {
        return marketingEmailsEnabled;
    }

    public void setMarketingEmailsEnabled(Boolean marketingEmailsEnabled) {
        this.marketingEmailsEnabled = marketingEmailsEnabled;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
}
