/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerDto, CreateRequest, UpdateRequest, Response, PhoneNumberDto, AddressDto, PrivacySettingsDto
 * Description: Defines request and response payloads for the customer REST API with OpenAPI and validation metadata.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.Customer.PhoneNumber;
import com.bobwares.customer.registration.Customer.PrivacySettings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public final class CustomerDto {

  private CustomerDto() {}

  @Schema(name = "CustomerCreateRequest", description = "Payload for creating a customer profile.")
  public record CreateRequest(
      @Schema(example = "Alice") @NotBlank String firstName,
      @Schema(example = "B.") @Size(max = 100) String middleName,
      @Schema(example = "Smith") @NotBlank String lastName,
      @Schema(description = "Unique email addresses")
          @NotNull @Size(min = 1) List<@Email @NotBlank String> emails,
      @Schema(description = "Phone numbers")
          @NotNull @Size(min = 1) List<@Valid PhoneNumberDto> phoneNumbers,
      @Schema(description = "Postal address") @Valid AddressDto address,
      @Schema(description = "Privacy preferences") @NotNull @Valid PrivacySettingsDto privacySettings) {}

  @Schema(name = "CustomerUpdateRequest", description = "Payload for updating a customer profile.")
  public record UpdateRequest(
      @Schema(example = "Alice") @NotBlank String firstName,
      @Schema(example = "B.") @Size(max = 100) String middleName,
      @Schema(example = "Smith") @NotBlank String lastName,
      @Schema(description = "Unique email addresses")
          @NotNull @Size(min = 1) List<@Email @NotBlank String> emails,
      @Schema(description = "Phone numbers")
          @NotNull @Size(min = 1) List<@Valid PhoneNumberDto> phoneNumbers,
      @Schema(description = "Postal address") @Valid AddressDto address,
      @Schema(description = "Privacy preferences") @NotNull @Valid PrivacySettingsDto privacySettings) {}

  @Schema(name = "CustomerResponse", description = "Customer profile representation")
  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      List<String> emails,
      List<PhoneNumberDto> phoneNumbers,
      AddressDto address,
      PrivacySettingsDto privacySettings,
      Instant createdAt,
      Instant updatedAt) {

    public static Response from(Customer customer) {
      AddressDto addressDto =
          customer.getAddress() == null
              ? null
              : new AddressDto(
                  customer.getAddress().getLine1(),
                  customer.getAddress().getLine2(),
                  customer.getAddress().getCity(),
                  customer.getAddress().getState(),
                  customer.getAddress().getPostalCode(),
                  customer.getAddress().getCountry());

      PrivacySettings privacy = customer.getPrivacySettings();
      PrivacySettingsDto privacyDto =
          new PrivacySettingsDto(
              privacy.isMarketingEmailsEnabled(), privacy.isTwoFactorEnabled());

      List<PhoneNumberDto> phoneNumberDtos =
          customer.getPhoneNumbers().stream()
              .map(CustomerDto::toDto)
              .collect(Collectors.toList());

      return new Response(
          customer.getId(),
          customer.getFirstName(),
          customer.getMiddleName(),
          customer.getLastName(),
          customer.getEmails().stream().toList(),
          phoneNumberDtos,
          addressDto,
          privacyDto,
          customer.getCreatedAt(),
          customer.getUpdatedAt());
    }
  }

  @Schema(name = "CustomerPhoneNumber")
  public record PhoneNumberDto(
      @Schema(example = "mobile")
          @NotBlank
          @Pattern(regexp = "(?i)mobile|home|work|other")
          String type,
      @Schema(example = "+14155550123")
          @NotBlank
          @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
          String number) {}

  @Schema(name = "CustomerAddress")
  public record AddressDto(
      @Schema(example = "123 Market St") @NotBlank @Size(max = 255) String line1,
      @Schema(example = "Suite 400") @Size(max = 255) String line2,
      @Schema(example = "San Francisco") @NotBlank @Size(max = 120) String city,
      @Schema(example = "CA") @NotBlank @Size(max = 120) String state,
      @Schema(example = "94103") @NotBlank @Size(max = 32) String postalCode,
      @Schema(example = "US") @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String country) {}

  @Schema(name = "CustomerPrivacySettings")
  public record PrivacySettingsDto(
      @Schema(description = "Marketing opt-in") boolean marketingEmailsEnabled,
      @Schema(description = "Two-factor enabled") boolean twoFactorEnabled) {}

  private static PhoneNumberDto toDto(PhoneNumber phoneNumber) {
    return new PhoneNumberDto(
        phoneNumber.getType().name().toLowerCase(Locale.ROOT), phoneNumber.getNumber());
  }
}
