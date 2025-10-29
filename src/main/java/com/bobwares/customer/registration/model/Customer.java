/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: Customer
 * Description: Root aggregate representing a registered customer and associated contact details.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "customer", schema = "customer_registration")
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "customer_id", nullable = false, updatable = false)
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

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private PostalAddress address;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "privacy_settings_id")
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

  public void addEmail(CustomerEmail email) {
    Objects.requireNonNull(email, "email");
    email.setCustomer(this);
    emails.add(email);
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    Objects.requireNonNull(phoneNumber, "phoneNumber");
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
}
