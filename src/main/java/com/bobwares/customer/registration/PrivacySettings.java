/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: PrivacySettings
 * Description: Embeddable privacy preferences capturing marketing and security opt-ins.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class PrivacySettings {

  @NotNull
  @Column(name = "marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled;

  @NotNull
  @Column(name = "two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled;

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
