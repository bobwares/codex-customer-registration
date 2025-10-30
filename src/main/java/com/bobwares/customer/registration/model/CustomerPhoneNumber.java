/**
 * App: Customer Registration Package: com.bobwares.customer.registration.model File:
 * CustomerPhoneNumber.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: CustomerPhoneNumber Description: Represents a typed phone number associated with a
 * customer for contact purposes.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer_phone_numbers")
public class CustomerPhoneNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JdbcTypeCode(SqlTypes.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "phone_type", nullable = false, length = 16)
  private PhoneNumberType type;

  @NotBlank
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  @Size(max = 32)
  @Column(name = "phone_number", nullable = false, length = 32)
  private String number;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public PhoneNumberType getType() {
    return type;
  }

  public void setType(PhoneNumberType type) {
    this.type = type;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
