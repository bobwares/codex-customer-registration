/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: PrivacySettings
 * Description: Embeddable privacy preferences controlling marketing communication and multifactor authentication state.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class PrivacySettings {

  @NotNull
  @Column(name = "privacy_marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled = Boolean.FALSE;

  @NotNull
  @Column(name = "privacy_two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled = Boolean.FALSE;

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
