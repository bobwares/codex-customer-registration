/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.model
 * File: PhoneNumber.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: PhoneNumber
 * Description: Embeddable value object representing a typed phone number in E.164 format.
 */
package com.bobwares.customer.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class PhoneNumber {

  @Enumerated(EnumType.STRING)
  @Column(name = "phone_type", nullable = false, length = 16)
  @NotNull
  private PhoneType type;

  @Column(name = "phone_number", nullable = false, length = 20)
  @NotBlank
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  private String number;

  public PhoneType getType() {
    return type;
  }

  public void setType(PhoneType type) {
    this.type = type;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
