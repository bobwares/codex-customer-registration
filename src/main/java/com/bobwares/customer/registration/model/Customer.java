/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: Customer
 * Description: Root aggregate representing a registered customer.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "customer_id", columnDefinition = "uuid")
  private UUID id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Size(max = 100)
  @Column(name = "middle_name", length = 100)
  private String middleName;

  @NotBlank
  @Size(max = 100)
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "address_id", nullable = false, unique = true)
  private PostalAddress address;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "privacy_settings_id", nullable = false, unique = true)
  private PrivacySettings privacySettings;

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<CustomerEmail> emails = new LinkedHashSet<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<CustomerPhoneNumber> phoneNumbers = new LinkedHashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public PostalAddress getAddress() {
    return address;
  }

  public void setAddress(PostalAddress address) {
    this.address = address;
  }

  public PrivacySettings getPrivacySettings() {
    return privacySettings;
  }

  public void setPrivacySettings(PrivacySettings privacySettings) {
    this.privacySettings = privacySettings;
  }

  public Set<CustomerEmail> getEmails() {
    return emails;
  }

  public void setEmails(Set<CustomerEmail> emails) {
    this.emails = emails;
  }

  public Set<CustomerPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<CustomerPhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void addEmail(CustomerEmail email) {
    email.setCustomer(this);
    emails.add(email);
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    phoneNumber.setCustomer(this);
    phoneNumbers.add(phoneNumber);
  }

  public void clearEmails() {
    emails.forEach(email -> email.setCustomer(null));
    emails.clear();
  }

  public void clearPhoneNumbers() {
    phoneNumbers.forEach(phone -> phone.setCustomer(null));
    phoneNumbers.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Customer customer)) {
      return false;
    }
    return id != null && Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
