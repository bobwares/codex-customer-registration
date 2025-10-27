/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerDto
 * Description: Transport objects for Customer REST APIs including validation rules.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.model.PhoneType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  public record Address(
      @NotBlank @Size(max = 255) String line1,
      @Size(max = 255) String line2,
      @NotBlank @Size(max = 100) String city,
      @NotBlank @Size(max = 100) String state,
      @NotBlank @Size(max = 20) String postalCode,
      @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String country) {}

  public record PrivacySettings(
      @NotNull Boolean marketingEmailsEnabled, @NotNull Boolean twoFactorEnabled) {}

  public record EmailAddress(@NotBlank @Email @Size(max = 255) String value) {}

  public record PhoneNumber(
      @NotNull PhoneType type,
      @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") @Size(max = 20) String number) {}

  public record CreateRequest(
      @NotBlank @Size(max = 100) String firstName,
      @Size(max = 100) String middleName,
      @NotBlank @Size(max = 100) String lastName,
      @Valid @NotNull Address address,
      @Valid @NotNull PrivacySettings privacySettings,
      @NotEmpty List<@Valid EmailAddress> emails,
      @NotEmpty List<@Valid PhoneNumber> phoneNumbers) {}

  public record UpdateRequest(
      @NotBlank @Size(max = 100) String firstName,
      @Size(max = 100) String middleName,
      @NotBlank @Size(max = 100) String lastName,
      @Valid @NotNull Address address,
      @Valid @NotNull PrivacySettings privacySettings,
      @NotEmpty List<@Valid EmailAddress> emails,
      @NotEmpty List<@Valid PhoneNumber> phoneNumbers) {}

  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      Address address,
      PrivacySettings privacySettings,
      List<String> emails,
      List<PhoneNumber> phoneNumbers) {}
}
