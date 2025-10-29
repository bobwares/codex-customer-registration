/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: CustomerPhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerPhoneNumber
 * Description: Stores normalized phone numbers for customers with type metadata.
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_phone_number", schema = "customer_registration")
public class CustomerPhoneNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "phone_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  private PhoneType type;

  @NotBlank
  @Size(max = 15)
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  @Column(name = "number", nullable = false, length = 15)
  private String number;
}
