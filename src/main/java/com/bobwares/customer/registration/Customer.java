/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: Customer, PhoneNumber, PhoneType, Address, PrivacySettings
 * Description: JPA aggregate that maps customer profiles, contact channels, and privacy preferences to PostgreSQL tables.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "id", nullable = false, updatable = false)
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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "customer_emails",
      joinColumns = @JoinColumn(name = "customer_id"))
  @Column(name = "email", nullable = false, length = 320)
  private Set<@NotBlank @Size(max = 320) String> emails = new LinkedHashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "customer_phone_numbers",
      joinColumns = @JoinColumn(name = "customer_id"))
  private Set<PhoneNumber> phoneNumbers = new LinkedHashSet<>();

  @Embedded private Address address;

  @Embedded
  @NotNull
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

  public Set<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
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

  @Embeddable
  public static class Address {

    @NotBlank
    @Size(max = 255)
    @Column(name = "address_line1", length = 255)
    private String line1;

    @Size(max = 255)
    @Column(name = "address_line2", length = 255)
    private String line2;

    @NotBlank
    @Size(max = 120)
    @Column(name = "address_city", length = 120)
    private String city;

    @NotBlank
    @Size(max = 120)
    @Column(name = "address_state", length = 120)
    private String state;

    @NotBlank
    @Size(max = 32)
    @Column(name = "address_postal_code", length = 32)
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "address_country", length = 2)
    private String country;

    public String getLine1() {
      return line1;
    }

    public void setLine1(String line1) {
      this.line1 = line1;
    }

    public String getLine2() {
      return line2;
    }

    public void setLine2(String line2) {
      this.line2 = line2;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }

    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }
  }

  @Embeddable
  public static class PrivacySettings {

    @Column(name = "marketing_emails_enabled", nullable = false)
    private boolean marketingEmailsEnabled;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    public boolean isMarketingEmailsEnabled() {
      return marketingEmailsEnabled;
    }

    public void setMarketingEmailsEnabled(boolean marketingEmailsEnabled) {
      this.marketingEmailsEnabled = marketingEmailsEnabled;
    }

    public boolean isTwoFactorEnabled() {
      return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
      this.twoFactorEnabled = twoFactorEnabled;
    }
  }

  @Embeddable
  public static class PhoneNumber {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private PhoneType type;

    @NotBlank
    @Size(max = 32)
    @Column(name = "number", nullable = false, length = 32)
    private String number;

    public PhoneType getType() {
      return type;
    }

    public void setType(PhoneType type) {
      this.type = type;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof PhoneNumber other)) {
        return false;
      }
      return type == other.type && Objects.equals(number, other.number);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, number);
    }
  }

  public enum PhoneType {
    MOBILE,
    HOME,
    WORK,
    OTHER
  }
}
