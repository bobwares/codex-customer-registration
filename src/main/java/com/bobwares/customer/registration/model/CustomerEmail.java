/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: CustomerEmail.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerEmail
 * Description: Represents an email address belonging to a customer with uniqueness per customer enforced.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_email", schema = "customer_registration")
public class CustomerEmail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "email_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @NotBlank
  @Email
  @Size(max = 320)
  @Column(name = "email", nullable = false, length = 320)
  private String email;
}
