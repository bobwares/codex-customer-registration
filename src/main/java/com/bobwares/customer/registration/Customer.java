/**
 * App: Customer Registration Package: com.bobwares.customer.registration File: Customer.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com) Date: 2025-10-29T19:49:40Z
 * Exports: Customer Description: JPA aggregate root that captures persisted customer identity,
 * contact, and privacy preferences for onboarding flows.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "first_name", length = 100, nullable = false)
  private String firstName;

  @Size(max = 100)
  @Column(name = "middle_name", length = 100)
  private String middleName;

  @NotBlank
  @Size(max = 100)
  @Column(name = "last_name", length = 100, nullable = false)
  private String lastName;

  @Size(max = 255)
  @Column(name = "address_line1", length = 255)
  private String addressLine1;

  @Size(max = 255)
  @Column(name = "address_line2", length = 255)
  private String addressLine2;

  @Size(max = 100)
  @Column(name = "address_city", length = 100)
  private String addressCity;

  @Size(max = 100)
  @Column(name = "address_state", length = 100)
  private String addressState;

  @Size(max = 20)
  @Column(name = "address_postal_code", length = 20)
  private String addressPostalCode;

  @Size(min = 2, max = 2)
  @Column(name = "address_country", length = 2)
  private String addressCountry;

  @NotNull
  @Column(name = "marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled;

  @NotNull
  @Column(name = "two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<CustomerEmail> emails = new ArrayList<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<CustomerPhoneNumber> phoneNumbers = new ArrayList<>();

  public UUID getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressState() {
    return addressState;
  }

  public void setAddressState(String addressState) {
    this.addressState = addressState;
  }

  public String getAddressPostalCode() {
    return addressPostalCode;
  }

  public void setAddressPostalCode(String addressPostalCode) {
    this.addressPostalCode = addressPostalCode;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
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

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public List<CustomerEmail> getEmails() {
    return emails;
  }

  public void setEmails(List<CustomerEmail> emails) {
    this.emails.clear();
    if (emails != null) {
      emails.forEach(this::addEmail);
    }
  }

  public void addEmail(CustomerEmail email) {
    email.setCustomer(this);
    this.emails.add(email);
  }

  public void removeEmail(CustomerEmail email) {
    email.setCustomer(null);
    this.emails.remove(email);
  }

  public List<CustomerPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<CustomerPhoneNumber> phoneNumbers) {
    this.phoneNumbers.clear();
    if (phoneNumbers != null) {
      phoneNumbers.forEach(this::addPhoneNumber);
    }
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    phoneNumber.setCustomer(this);
    this.phoneNumbers.add(phoneNumber);
  }

  public void removePhoneNumber(CustomerPhoneNumber phoneNumber) {
    phoneNumber.setCustomer(null);
    this.phoneNumbers.remove(phoneNumber);
  }
}
