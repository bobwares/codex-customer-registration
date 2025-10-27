/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerDto
 * Description: DTO definitions for customer REST operations including create, update, and response payloads.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.PhoneType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  @Schema(name = "CustomerCreateRequest", description = "Request body for creating a customer")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class CreateRequest {

    @NotBlank
    @Schema(example = "Ada")
    private String firstName;

    @Schema(example = "Grace")
    private String middleName;

    @NotBlank
    @Schema(example = "Lovelace")
    private String lastName;

    @NotEmpty
    @Size(max = 5)
    private List<@Email String> emails = new ArrayList<>();

    @Valid
    @NotEmpty
    private List<PhoneNumberPayload> phoneNumbers = new ArrayList<>();

    @Valid
    @NotNull
    private PostalAddressPayload address;

    @Valid
    @NotNull
    private PrivacySettingsPayload privacySettings;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getMiddleName() {
      return middleName;
    }

    public void setMiddleName(String middleName) {
      this.middleName = middleName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public List<String> getEmails() {
      return emails;
    }

    public void setEmails(List<String> emails) {
      this.emails = emails;
    }

    public List<PhoneNumberPayload> getPhoneNumbers() {
      return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberPayload> phoneNumbers) {
      this.phoneNumbers = phoneNumbers;
    }

    public PostalAddressPayload getAddress() {
      return address;
    }

    public void setAddress(PostalAddressPayload address) {
      this.address = address;
    }

    public PrivacySettingsPayload getPrivacySettings() {
      return privacySettings;
    }

    public void setPrivacySettings(PrivacySettingsPayload privacySettings) {
      this.privacySettings = privacySettings;
    }
  }

  @Schema(name = "CustomerUpdateRequest", description = "Request body for updating a customer")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class UpdateRequest extends CreateRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }
  }

  @Schema(name = "CustomerResponse", description = "Customer response payload")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Response {

    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
    private List<String> emails;
    private List<PhoneNumberPayload> phoneNumbers;
    private PostalAddressPayload address;
    private PrivacySettingsPayload privacySettings;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getMiddleName() {
      return middleName;
    }

    public void setMiddleName(String middleName) {
      this.middleName = middleName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public List<String> getEmails() {
      return emails;
    }

    public void setEmails(List<String> emails) {
      this.emails = emails;
    }

    public List<PhoneNumberPayload> getPhoneNumbers() {
      return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberPayload> phoneNumbers) {
      this.phoneNumbers = phoneNumbers;
    }

    public PostalAddressPayload getAddress() {
      return address;
    }

    public void setAddress(PostalAddressPayload address) {
      this.address = address;
    }

    public PrivacySettingsPayload getPrivacySettings() {
      return privacySettings;
    }

    public void setPrivacySettings(PrivacySettingsPayload privacySettings) {
      this.privacySettings = privacySettings;
    }
  }

  @Schema(name = "CustomerPhoneNumber", description = "Phone number details")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class PhoneNumberPayload {

    @NotNull
    private PhoneType type;

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

  @Schema(name = "CustomerAddress", description = "Postal address payload")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class PostalAddressPayload {

    @NotBlank
    private String line1;

    private String line2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$")
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

  @Schema(name = "CustomerPrivacySettings", description = "Privacy preferences payload")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class PrivacySettingsPayload {

    @NotNull
    private Boolean marketingEmailsEnabled;

    @NotNull
    private Boolean twoFactorEnabled;

    public Boolean getMarketingEmailsEnabled() {
      return marketingEmailsEnabled;
    }

    public void setMarketingEmailsEnabled(Boolean marketingEmailsEnabled) {
      this.marketingEmailsEnabled = marketingEmailsEnabled;
    }

    public Boolean getTwoFactorEnabled() {
      return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
      this.twoFactorEnabled = twoFactorEnabled;
    }
  }
}
