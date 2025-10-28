package com.bobwares.customer.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PrivacySettings {

    @Column(name = "marketing_emails_enabled", nullable = false)
    private boolean marketingEmailsEnabled;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    public PrivacySettings() {
    }

    public PrivacySettings(boolean marketingEmailsEnabled, boolean twoFactorEnabled) {
        this.marketingEmailsEnabled = marketingEmailsEnabled;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public boolean isMarketingEmailsEnabled() {
        return marketingEmailsEnabled;
    }

    public void setMarketingEmailsEnabled(boolean marketingEmailsEnabled) {
        this.marketingEmailsEnabled = marketingEmailsEnabled;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
}
