/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: Customer
 * Description: JPA aggregate representing a registered customer with contact methods and privacy settings.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "middle_name", length = 100)
  private String middleName;

  @NotBlank
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @NotEmpty
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "customer_emails", joinColumns = @JoinColumn(name = "customer_id"))
  @Column(name = "email", nullable = false, length = 255)
  private List<String> emails = new ArrayList<>();

  @NotEmpty
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "customer_phone_numbers", joinColumns = @JoinColumn(name = "customer_id"))
  private List<PhoneNumber> phoneNumbers = new ArrayList<>();

  @NotNull
  @Embedded
  private PostalAddress address;

  @NotNull
  @Embedded
  private PrivacySettings privacySettings;

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

  public List<String> getEmails() {
    return emails;
  }

  public void setEmails(List<String> emails) {
    this.emails = emails;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
