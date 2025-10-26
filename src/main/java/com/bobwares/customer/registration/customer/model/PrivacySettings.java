/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.model
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: PrivacySettings
 * Description: JPA entity capturing marketing and security preferences for a customer.
 */
package com.bobwares.customer.registration.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "privacy_settings")
public class PrivacySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "marketing_emails_enabled", nullable = false)
    private boolean marketingEmailsEnabled;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    public Long getId() {
        return id;
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
