/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: PrivacySettings
 * Description: Entity representing customer privacy preferences.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privacy_settings")
@Getter
@Setter
@NoArgsConstructor
public class PrivacySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_settings_id")
    private Long privacySettingsId;

    @NotNull
    @Column(name = "marketing_emails_enabled", nullable = false)
    private Boolean marketingEmailsEnabled;

    @NotNull
    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled;
}
