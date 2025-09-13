/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: codex
 * Date: 2025-09-13T02:16:18Z
 * Exports: PrivacySettings
 * Description: Embeddable representing privacy settings for a customer.
 */
package com.bobwares.customer.registration.customer;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PrivacySettings {

    private boolean marketingEmailsEnabled;

    private boolean twoFactorEnabled;
}
