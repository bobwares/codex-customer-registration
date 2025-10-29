/**
 * App: Customer Registration Package: com.bobwares.customer.registration.api File: CustomerDto.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com) Date: 2025-10-29T19:49:40Z
 * Exports: CustomerDto Description: Defines request and response payloads for the customer REST API
 * including validation and schema metadata.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.PhoneContactType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class CustomerDto {

  private CustomerDto() {}

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerCreateRequest", description = "Payload for creating a customer")
  public static class CreateRequest {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String middleName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotEmpty private List<String> emails;

    @Valid private List<PhoneNumber> phoneNumbers;

    @Valid private Address address;

    @Valid @NotNull private PrivacySettings privacySettings;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerUpdateRequest", description = "Payload for updating a customer")
  public static class UpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String middleName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotEmpty private List<String> emails;

    @Valid private List<PhoneNumber> phoneNumbers;

    @Valid private Address address;

    @Valid @NotNull private PrivacySettings privacySettings;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerResponse", description = "Response representation of a customer")
  public static class Response {

    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Address address;
    private List<String> emails;
    private List<PhoneNumber> phoneNumbers;
    private PrivacySettings privacySettings;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerAddress", description = "Postal address for a customer")
  public static class Address {
    @Size(max = 255)
    private String line1;

    @Size(max = 255)
    private String line2;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 20)
    private String postalCode;

    @Size(min = 2, max = 2)
    private String country;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerPrivacySettings", description = "Privacy preferences for the customer")
  public static class PrivacySettings {
    @NotNull private Boolean marketingEmailsEnabled;
    @NotNull private Boolean twoFactorEnabled;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(name = "CustomerPhoneNumber", description = "Phone number entry for the customer")
  public static class PhoneNumber {
    @NotNull private PhoneContactType type;

    @NotBlank private String number;
  }
}
