/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * CustomerPhoneNumber.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T19:49:40Z Exports: CustomerPhoneNumber Description: Represents a customer's
 * phone number with type metadata and normalization helpers for persistence.
 */
package com.bobwares.customer.registration;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "customer_phone_numbers")
public class CustomerPhoneNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "contact_type", nullable = false)
  private PhoneContactType contactType;

  @NotBlank
  @Size(max = 32)
  @Column(name = "phone_number", length = 32, nullable = false)
  private String phoneNumber;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public PhoneContactType getContactType() {
    return contactType;
  }

  public void setContactType(PhoneContactType contactType) {
    this.contactType = contactType;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
