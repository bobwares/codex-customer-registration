/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: PrivacySettings
 * Description: JPA entity capturing marketing and authentication privacy preferences.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "privacy_settings")
public class PrivacySettings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "privacy_settings_id")
  private Long id;

  @NotNull
  @Column(name = "marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled;

  @NotNull
  @Column(name = "two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
