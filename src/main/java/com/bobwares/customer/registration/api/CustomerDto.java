/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerDto
 * Description: API request and response records describing the customer registration contract.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.PhoneNumberType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  @Schema(name = "CustomerCreateRequest")
  public record CreateRequest(
      @NotBlank @Size(max = 80) String firstName,
      @Size(max = 80) String middleName,
      @NotBlank @Size(max = 80) String lastName,
      @NotEmpty Set<@Email @NotBlank @Size(max = 320) String> emails,
      @Valid @NotNull PostalAddressRequest address,
      @Valid @NotNull PrivacySettingsRequest privacySettings,
      @Valid List<PhoneNumberRequest> phoneNumbers) {}

  @Schema(name = "CustomerUpdateRequest")
  public record UpdateRequest(
      @NotBlank @Size(max = 80) String firstName,
      @Size(max = 80) String middleName,
      @NotBlank @Size(max = 80) String lastName,
      @NotEmpty Set<@Email @NotBlank @Size(max = 320) String> emails,
      @Valid @NotNull PostalAddressRequest address,
      @Valid @NotNull PrivacySettingsRequest privacySettings,
      @Valid List<PhoneNumberRequest> phoneNumbers) {}

  @Schema(name = "CustomerResponse")
  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      String primaryEmail,
      Set<String> emails,
      List<PhoneNumberRequest> phoneNumbers,
      PostalAddressRequest address,
      PrivacySettingsRequest privacySettings,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt) {}

  @Schema(name = "CustomerPhoneNumber")
  public record PhoneNumberRequest(
      @NotNull PhoneNumberType type,
      @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") String number) {}

  @Schema(name = "CustomerPostalAddress")
  public record PostalAddressRequest(
      @NotBlank @Size(max = 128) String line1,
      @Size(max = 128) String line2,
      @NotBlank @Size(max = 96) String city,
      @NotBlank @Size(max = 96) String state,
      @NotBlank @Size(max = 24) String postalCode,
      @NotBlank @Size(min = 2, max = 2) String country) {}

  @Schema(name = "CustomerPrivacySettings")
  public record PrivacySettingsRequest(
      @NotNull Boolean marketingEmailsEnabled, @NotNull Boolean twoFactorEnabled) {}

  @Schema(name = "CustomerSummary")
  public record Summary(
      UUID id,
      String firstName,
      String lastName,
      String primaryEmail,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt) {}

  @Schema(name = "CustomerMetaResponse")
  public record MetaResponse(BigDecimal defaultTaxRate, BigDecimal defaultShippingCost, List<String> supportedCurrencies) {}
}
