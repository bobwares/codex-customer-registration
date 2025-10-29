/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: PrivacySettings
 * Description: Captures marketing and security preferences linked to a customer.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "privacy_settings", schema = "customer_registration")
public class PrivacySettings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "privacy_settings_id")
  private Integer id;

  @NotNull
  @Column(name = "marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled;

  @NotNull
  @Column(name = "two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled;
}
