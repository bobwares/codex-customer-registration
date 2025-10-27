/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: PostalAddress
 * Description: JPA entity that models a normalized postal address for a customer.
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
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "postal_address")
public class PostalAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

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
  @Size(max = 100)
  @Column(name = "state", nullable = false, length = 100)
  private String state;

  @NotBlank
  @Size(max = 20)
  @Column(name = "postal_code", nullable = false, length = 20)
  private String postalCode;

  @NotBlank
  @Size(min = 2, max = 2)
  @Column(name = "country", nullable = false, length = 2)
  private String country;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
