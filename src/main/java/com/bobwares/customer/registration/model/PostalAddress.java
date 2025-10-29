/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: PostalAddress
 * Description: Represents a normalized postal address referenced by customer records.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "postal_address", schema = "customer_registration")
public class PostalAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Integer id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "line1", nullable = false, length = 255)
  private String line1;

  @Size(max = 255)
  @Column(name = "line2", length = 255)
  private String line2;

  @NotBlank
  @Size(max = 100)
  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @NotBlank
  @Size(max = 50)
  @Column(name = "state", nullable = false, length = 50)
  private String state;

  @Size(max = 20)
  @Column(name = "postal_code", length = 20)
  private String postalCode;

  @NotBlank
  @Size(min = 2, max = 2)
  @Column(name = "country", nullable = false, length = 2)
  private String country;
}
