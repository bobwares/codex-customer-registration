/**
 * App: Customer Registration Package: com.bobwares.customer.registration.model File:
 * PrivacySettings.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: PrivacySettings Description: Encapsulates customer privacy preferences stored as
 * embedded columns on the customer entity.
 */
package com.bobwares.customer.registration.model;

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
