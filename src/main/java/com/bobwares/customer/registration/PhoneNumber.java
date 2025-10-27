/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: PhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: PhoneNumber
 * Description: Embeddable value object capturing a customer's phone number and classification.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class PhoneNumber {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "phone_type", nullable = false, length = 16)
  private PhoneType type;

  @NotBlank
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  @Column(name = "phone_number", nullable = false, length = 32)
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
