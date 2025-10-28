/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: PrivacySettings
 * Description: Embeddable structure storing user privacy and security preferences for customer accounts.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettings {

    @Column(name = "privacy_marketing_emails_enabled", nullable = false)
    private boolean marketingEmailsEnabled;

    @Column(name = "privacy_two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;
}
