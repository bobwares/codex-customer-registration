/**
 * App: Customer Registration Package: com.bobwares.customer.registration.model File:
 * PostalAddress.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: PostalAddress Description: Represents an embeddable postal address captured for each
 * customer profile.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public class PostalAddress {

  @NotBlank
  @Size(max = 255)
  @Column(name = "address_line1", nullable = false, length = 255)
  private String line1;

  @Size(max = 255)
  @Column(name = "address_line2", length = 255)
  private String line2;

  @NotBlank
  @Size(max = 100)
  @Column(name = "address_city", nullable = false, length = 100)
  private String city;

  @NotBlank
  @Size(max = 100)
  @Column(name = "address_state", nullable = false, length = 100)
  private String state;

  @NotBlank
  @Size(max = 20)
  @Column(name = "address_postal_code", nullable = false, length = 20)
  private String postalCode;

  @NotBlank
  @Size(min = 2, max = 2)
  @Column(name = "address_country", nullable = false, length = 2)
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
