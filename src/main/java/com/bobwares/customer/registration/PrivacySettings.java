/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: PrivacySettings
 * Description: JPA entity representing customer privacy settings.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "privacy_settings")
public class PrivacySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_settings_id")
    private Integer privacySettingsId;

    @NotNull
    @Column(name = "marketing_emails_enabled", nullable = false)
    private Boolean marketingEmailsEnabled;

    @NotNull
    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled;

    public Integer getPrivacySettingsId() {
        return privacySettingsId;
    }

    public void setPrivacySettingsId(Integer privacySettingsId) {
        this.privacySettingsId = privacySettingsId;
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
