/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: Turn 1
 * Author: Bobwares
 * Date: 2025-10-30T08:07:15Z
 * Exports: CustomerDto
 * Description: Request and response data transfer objects for the customer REST API.
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
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  public record CreateRequest(
      @NotBlank String firstName,
      String middleName,
      @NotBlank String lastName,
      @NotEmpty Set<@Email String> emails,
      @Valid List<@NotNull PhoneNumberPayload> phoneNumbers,
      @Valid AddressPayload address,
      @Valid @NotNull PrivacyPayload privacySettings) {}

  public record UpdateRequest(
      @NotBlank String firstName,
      String middleName,
      @NotBlank String lastName,
      @NotEmpty Set<@Email String> emails,
      @Valid List<@NotNull PhoneNumberPayload> phoneNumbers,
      @Valid AddressPayload address,
      @Valid @NotNull PrivacyPayload privacySettings) {}

  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      Set<String> emails,
      List<PhoneNumberPayload> phoneNumbers,
      AddressPayload address,
      PrivacyPayload privacySettings,
      Instant createdAt,
      Instant updatedAt) {}

  public record PhoneNumberPayload(
      @NotNull PhoneType type,
      @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") @Size(max = 20) String number) {}

  public record AddressPayload(
      @NotBlank String line1,
      String line2,
      @NotBlank String city,
      @NotBlank String state,
      @NotBlank String postalCode,
      @NotBlank @Size(min = 2, max = 2) String country) {}

  public record PrivacyPayload(
      @NotNull Boolean marketingEmailsEnabled,
      @NotNull Boolean twoFactorEnabled) {}
}
