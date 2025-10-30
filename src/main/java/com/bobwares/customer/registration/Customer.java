/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Customer.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: Customer
 * Description: JPA aggregate that models a customer profile with contact and privacy preferences.
 */
package com.bobwares.customer.registration;

import com.bobwares.customer.registration.model.PhoneNumber;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @NotBlank
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @ElementCollection
  @CollectionTable(
      name = "customer_emails",
      joinColumns = @JoinColumn(name = "customer_id"),
      uniqueConstraints =
          @UniqueConstraint(name = "uk_customer_emails_email", columnNames = "email"))
  @Column(name = "email", nullable = false)
  @NotEmpty
  private Set<@Size(max = 320) String> emails = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(name = "customer_phone_numbers", joinColumns = @JoinColumn(name = "customer_id"))
  private List<PhoneNumber> phoneNumbers;

  @Embedded
  private PostalAddress address;

  @Embedded
  @NotNull
  private PrivacySettings privacySettings;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
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

  public Set<String> getEmails() {
    return emails;
  }

  public void setEmails(Set<String> emails) {
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
