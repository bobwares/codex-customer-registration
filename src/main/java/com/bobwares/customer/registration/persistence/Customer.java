/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.persistence
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: Customer
 * Description: Aggregate root entity mapping the customer profile domain to the PostgreSQL schema.
 */
package com.bobwares.customer.registration.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
    name = "customer",
    schema = "customer_registration",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_customer_address", columnNames = {"address_id"})
    })
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "customer_id")
  private UUID id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "first_name", nullable = false, length = 255)
  private String firstName;

  @Size(max = 255)
  @Column(name = "middle_name", length = 255)
  private String middleName;

  @NotBlank
  @Size(max = 255)
  @Column(name = "last_name", nullable = false, length = 255)
  private String lastName;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "privacy_settings_id", nullable = false)
  private PrivacySettings privacySettings;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address_id")
  private CustomerAddress address;

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

  public PrivacySettings getPrivacySettings() {
    return privacySettings;
  }

  public void setPrivacySettings(PrivacySettings privacySettings) {
    this.privacySettings = privacySettings;
  }

  public CustomerAddress getAddress() {
    return address;
  }

  public void setAddress(CustomerAddress address) {
    this.address = address;
  }

  public Set<CustomerEmail> getEmails() {
    return emails;
  }

  public void setEmails(Set<CustomerEmail> emails) {
    this.emails.clear();
    if (emails != null) {
      emails.forEach(this::addEmail);
    }
  }

  public void addEmail(CustomerEmail email) {
    Objects.requireNonNull(email, "email must not be null");
    email.setCustomer(this);
    emails.add(email);
  }

  public void removeEmail(CustomerEmail email) {
    if (email != null) {
      email.setCustomer(null);
      emails.remove(email);
    }
  }

  public Set<CustomerPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<CustomerPhoneNumber> phoneNumbers) {
    this.phoneNumbers.clear();
    if (phoneNumbers != null) {
      phoneNumbers.forEach(this::addPhoneNumber);
    }
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    Objects.requireNonNull(phoneNumber, "phoneNumber must not be null");
    phoneNumber.setCustomer(this);
    phoneNumbers.add(phoneNumber);
  }

  public void removePhoneNumber(CustomerPhoneNumber phoneNumber) {
    if (phoneNumber != null) {
      phoneNumber.setCustomer(null);
      phoneNumbers.remove(phoneNumber);
    }
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
