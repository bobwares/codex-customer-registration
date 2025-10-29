/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerDto
 * Description: Declares request and response payloads for customer CRUD operations with validation metadata.
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

  public record PrivacySettingsDto(
      @NotNull Boolean marketingEmailsEnabled, @NotNull Boolean twoFactorEnabled) {}

  public record AddressDto(
      @NotBlank @Size(max = 255) String line1,
      @Size(max = 255) String line2,
      @NotBlank @Size(max = 100) String city,
      @NotBlank @Size(max = 50) String state,
      @Size(max = 20) String postalCode,
      @NotBlank @Size(min = 2, max = 2) String country) {}

  public record PhoneNumberDto(
      @NotNull PhoneType type,
      @NotBlank @Size(max = 15) @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") String number) {}

  public record CreateRequest(
      @NotBlank @Size(max = 255) String firstName,
      @Size(max = 255) String middleName,
      @NotBlank @Size(max = 255) String lastName,
      @NotEmpty List<@Email @NotBlank String> emails,
      @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers,
      @Valid AddressDto address,
      @Valid @NotNull PrivacySettingsDto privacySettings) {}

  public record UpdateRequest(
      @NotBlank @Size(max = 255) String firstName,
      @Size(max = 255) String middleName,
      @NotBlank @Size(max = 255) String lastName,
      @NotEmpty List<@Email @NotBlank String> emails,
      @NotEmpty List<@Valid PhoneNumberDto> phoneNumbers,
      @Valid AddressDto address,
      @Valid @NotNull PrivacySettingsDto privacySettings) {}

  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      List<String> emails,
      List<PhoneNumberDto> phoneNumbers,
      AddressDto address,
      PrivacySettingsDto privacySettings) {}
}
