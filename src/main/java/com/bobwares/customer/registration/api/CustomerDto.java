/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerDto
 * Description: Data transfer objects for customer CRUD operations and OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.model.PhoneNumberType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  public record PrivacySettingsDto(
      @NotNull Boolean marketingEmailsEnabled,
      @NotNull Boolean twoFactorEnabled) {}

  public record AddressDto(
      @NotBlank @Size(max = 255) String line1,
      @Size(max = 255) String line2,
      @NotBlank @Size(max = 100) String city,
      @NotBlank @Size(max = 100) String state,
      @Size(max = 20) String postalCode,
      @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String country) {}

  public record PhoneNumberDto(
      @NotNull PhoneNumberType type,
      @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") String number) {}

  public record CreateRequest(
      @NotBlank @Size(max = 255) String firstName,
      @Size(max = 255) String middleName,
      @NotBlank @Size(max = 255) String lastName,
      @Valid @NotNull PrivacySettingsDto privacySettings,
      @Valid AddressDto address,
      @NotEmpty List<@Email @Size(max = 320) String> emails,
      @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers) {}

  public record UpdateRequest(
      @NotBlank @Size(max = 255) String firstName,
      @Size(max = 255) String middleName,
      @NotBlank @Size(max = 255) String lastName,
      @Valid @NotNull PrivacySettingsDto privacySettings,
      @Valid AddressDto address,
      @NotEmpty List<@Email @Size(max = 320) String> emails,
      @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers) {}

  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      PrivacySettingsDto privacySettings,
      AddressDto address,
      List<String> emails,
      List<PhoneNumberDto> phoneNumbers,
      Instant createdAt,
      Instant updatedAt) {}
}
