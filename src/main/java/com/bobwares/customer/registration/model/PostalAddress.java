/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: PostalAddress
 * Description: Embeddable structure capturing physical mailing address details for a customer.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public class PostalAddress {

  @Column(name = "address_line1", nullable = false)
  @NotBlank
  private String line1;

  @Column(name = "address_line2")
  private String line2;

  @Column(name = "city", nullable = false)
  @NotBlank
  private String city;

  @Column(name = "state", nullable = false)
  @NotBlank
  private String state;

  @Column(name = "postal_code", nullable = false)
  @NotBlank
  private String postalCode;

  @Column(name = "country", nullable = false, length = 2)
  @NotBlank
  @Size(min = 2, max = 2)
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
