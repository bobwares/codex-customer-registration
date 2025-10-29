/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: Customer
 * Description: Aggregate root representing a registered customer with contact, address, and privacy metadata.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
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
    name = "customers",
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_customers_email", columnNames = {"primary_email"})
    })
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 80)
  @Column(name = "first_name", nullable = false, length = 80)
  private String firstName;

  @Size(max = 80)
  @Column(name = "middle_name", length = 80)
  private String middleName;

  @NotBlank
  @Size(max = 80)
  @Column(name = "last_name", nullable = false, length = 80)
  private String lastName;

  @Email
  @NotBlank
  @Size(max = 320)
  @Column(name = "primary_email", nullable = false, length = 320)
  private String primaryEmail;

  @NotEmpty
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "customer_emails",
      joinColumns = @JoinColumn(name = "customer_id"),
      uniqueConstraints =
          @UniqueConstraint(name = "uk_customer_emails_email", columnNames = {"email"}))
  @Column(name = "email", nullable = false, length = 320)
  private Set<@Email @NotBlank @Size(max = 320) String> emails = new LinkedHashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "customer_phone_numbers", joinColumns = @JoinColumn(name = "customer_id"))
  private Set<@Valid PhoneNumber> phoneNumbers = new LinkedHashSet<>();

  @NotNull
  @Valid
  @Embedded
  private PostalAddress address;

  @NotNull
  @Valid
  @Embedded
  private PrivacySettings privacySettings;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

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

  public String getPrimaryEmail() {
    return primaryEmail;
  }

  public void setPrimaryEmail(String primaryEmail) {
    this.primaryEmail = primaryEmail;
  }

  public Set<String> getEmails() {
    return emails;
  }

  public void setEmails(Set<String> emails) {
    this.emails = emails;
  }

  public Set<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
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

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void addEmail(String email) {
    if (email != null) {
      emails.add(email);
    }
  }

  public void removeEmail(String email) {
    emails.remove(email);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Customer)) {
      return false;
    }
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
