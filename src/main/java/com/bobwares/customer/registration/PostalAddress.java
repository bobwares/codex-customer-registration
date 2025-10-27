/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: PostalAddress
 * Description: Embeddable value object representing a customer's mailing address with validation constraints.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class PostalAddress {

  @NotBlank
  @Column(name = "address_line1", length = 255)
  private String line1;

  @Column(name = "address_line2", length = 255)
  private String line2;

  @NotBlank
  @Column(name = "address_city", length = 120)
  private String city;

  @NotBlank
  @Column(name = "address_state", length = 120)
  private String state;

  @NotBlank
  @Column(name = "address_postal_code", length = 32)
  private String postalCode;

  @NotBlank
  @Pattern(regexp = "^[A-Z]{2}$")
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
