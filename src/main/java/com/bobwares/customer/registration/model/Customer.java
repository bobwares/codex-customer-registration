/**
 * App: Customer Registration Package: com.bobwares.customer.registration.model File: Customer.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z Exports: Customer
 * Description: Aggregates the customer domain model with embedded address, privacy settings, and
 * related contact collections.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "customers",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"first_name", "last_name", "address_line1"})
    })
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JdbcTypeCode(SqlTypes.UUID)
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

  @Embedded @Valid private PostalAddress address = new PostalAddress();

  @Embedded @Valid private PrivacySettings privacySettings = new PrivacySettings();

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

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
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
    Objects.requireNonNull(email, "email");
    email.setCustomer(this);
    this.emails.add(email);
  }

  public void setPhoneNumbers(List<CustomerPhoneNumber> phoneNumbers) {
    this.phoneNumbers.clear();
    if (phoneNumbers != null) {
      phoneNumbers.forEach(this::addPhoneNumber);
    }
  }

  public List<CustomerPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    Objects.requireNonNull(phoneNumber, "phoneNumber");
    phoneNumber.setCustomer(this);
    this.phoneNumbers.add(phoneNumber);
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
