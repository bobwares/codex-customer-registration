/**
 * App: Customer Registration Package: com.bobwares.customer.registration.api File: CustomerDto.java
 * Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z Exports: CustomerDto,
 * CustomerDto.CreateRequest, CustomerDto.UpdateRequest, CustomerDto.Response Description: Declares
 * request and response payloads for customer CRUD endpoints with validation and OpenAPI metadata.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.CustomerEmail;
import com.bobwares.customer.registration.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.model.PhoneNumberType;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

public final class CustomerDto {

  private CustomerDto() {}

  public static Customer toEntity(CreateRequest request) {
    Objects.requireNonNull(request, "request");
    Customer customer = new Customer();
    populateEntity(
        customer,
        request.getFirstName(),
        request.getMiddleName(),
        request.getLastName(),
        request.getAddress(),
        request.getPrivacy(),
        request.getEmails(),
        request.getPhoneNumbers());
    return customer;
  }

  public static void updateEntity(Customer customer, UpdateRequest request) {
    Objects.requireNonNull(customer, "customer");
    Objects.requireNonNull(request, "request");
    populateEntity(
        customer,
        request.getFirstName(),
        request.getMiddleName(),
        request.getLastName(),
        request.getAddress(),
        request.getPrivacy(),
        request.getEmails(),
        request.getPhoneNumbers());
  }

  private static void populateEntity(
      Customer customer,
      String firstName,
      String middleName,
      String lastName,
      Address address,
      Privacy privacy,
      List<String> emails,
      List<PhoneNumber> phoneNumbers) {
    customer.setFirstName(firstName);
    customer.setMiddleName(middleName);
    customer.setLastName(lastName);

    PostalAddress postalAddress = customer.getAddress();
    postalAddress.setLine1(address.getLine1());
    postalAddress.setLine2(address.getLine2());
    postalAddress.setCity(address.getCity());
    postalAddress.setState(address.getState());
    postalAddress.setPostalCode(address.getPostalCode());
    postalAddress.setCountry(address.getCountry());

    PrivacySettings privacySettings = customer.getPrivacySettings();
    privacySettings.setMarketingEmailsEnabled(privacy.getMarketingEmailsEnabled());
    privacySettings.setTwoFactorEnabled(privacy.getTwoFactorEnabled());

    reconcileEmails(customer, emails);
    reconcilePhoneNumbers(customer, phoneNumbers);
  }

  private static CustomerEmail toCustomerEmail(String email) {
    CustomerEmail entity = new CustomerEmail();
    entity.setEmail(email);
    return entity;
  }

  private static void reconcileEmails(Customer customer, List<String> desiredEmails) {
    List<CustomerEmail> current = customer.getEmails();
    if (CollectionUtils.isEmpty(desiredEmails)) {
      current.clear();
      return;
    }

    List<String> normalized = desiredEmails.stream().map(String::toLowerCase).distinct().toList();
    current.removeIf(existing -> !normalized.contains(existing.getEmail().toLowerCase()));

    List<String> existingValues =
        current.stream().map(email -> email.getEmail().toLowerCase()).toList();
    normalized.stream()
        .filter(email -> !existingValues.contains(email))
        .map(CustomerDto::toCustomerEmail)
        .forEach(customer::addEmail);
  }

  private static void reconcilePhoneNumbers(
      Customer customer, List<PhoneNumber> desiredPhoneNumbers) {
    customer.getPhoneNumbers().clear();
    if (!CollectionUtils.isEmpty(desiredPhoneNumbers)) {
      desiredPhoneNumbers.stream()
          .map(CustomerDto::toCustomerPhoneNumber)
          .forEach(customer::addPhoneNumber);
    }
  }

  private static CustomerPhoneNumber toCustomerPhoneNumber(PhoneNumber phoneNumber) {
    CustomerPhoneNumber entity = new CustomerPhoneNumber();
    entity.setType(PhoneNumberType.valueOf(phoneNumber.getType().name()));
    entity.setNumber(phoneNumber.getNumber());
    return entity;
  }

  public static Response fromEntity(Customer customer) {
    Response response = new Response();
    response.id = customer.getId();
    response.firstName = customer.getFirstName();
    response.middleName = customer.getMiddleName();
    response.lastName = customer.getLastName();

    PostalAddress postalAddress = customer.getAddress();
    Address address = new Address();
    address.setLine1(postalAddress.getLine1());
    address.setLine2(postalAddress.getLine2());
    address.setCity(postalAddress.getCity());
    address.setState(postalAddress.getState());
    address.setPostalCode(postalAddress.getPostalCode());
    address.setCountry(postalAddress.getCountry());
    response.address = address;

    PrivacySettings privacySettings = customer.getPrivacySettings();
    Privacy privacy = new Privacy();
    privacy.setMarketingEmailsEnabled(privacySettings.getMarketingEmailsEnabled());
    privacy.setTwoFactorEnabled(privacySettings.getTwoFactorEnabled());
    response.privacy = privacy;

    response.emails = customer.getEmails().stream().map(CustomerEmail::getEmail).toList();
    response.phoneNumbers =
        customer.getPhoneNumbers().stream()
            .map(
                phoneNumber -> {
                  PhoneNumber dto = new PhoneNumber();
                  dto.setType(PhoneNumber.Type.valueOf(phoneNumber.getType().name()));
                  dto.setNumber(phoneNumber.getNumber());
                  return dto;
                })
            .toList();
    return response;
  }

  public static class CreateRequest {

    @NotBlank
    @Length(max = 100)
    private String firstName;

    @Length(max = 100)
    private String middleName;

    @NotBlank
    @Length(max = 100)
    private String lastName;

    @Valid @NotNull private Address address;

    @Valid @NotNull private Privacy privacy;

    @NotEmpty private List<@Email @Size(max = 255) String> emails;

    private List<@Valid PhoneNumber> phoneNumbers;

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

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public Privacy getPrivacy() {
      return privacy;
    }

    public void setPrivacy(Privacy privacy) {
      this.privacy = privacy;
    }

    public List<String> getEmails() {
      return emails;
    }

    public void setEmails(List<String> emails) {
      this.emails = emails;
    }

    public List<PhoneNumber> getPhoneNumbers() {
      return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
      this.phoneNumbers = phoneNumbers;
    }
  }

  public static class UpdateRequest extends CreateRequest {}

  public static class Response {

    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Address address;
    private Privacy privacy;
    private List<String> emails;
    private List<PhoneNumber> phoneNumbers;

    public UUID getId() {
      return id;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getMiddleName() {
      return middleName;
    }

    public String getLastName() {
      return lastName;
    }

    public Address getAddress() {
      return address;
    }

    public Privacy getPrivacy() {
      return privacy;
    }

    public List<String> getEmails() {
      return emails;
    }

    public List<PhoneNumber> getPhoneNumbers() {
      return phoneNumbers;
    }
  }

  public static class Address {

    @NotBlank
    @Size(max = 255)
    private String line1;

    @Size(max = 255)
    private String line2;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String state;

    @NotBlank
    @Size(max = 20)
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

  public static class Privacy {

    @NotNull private Boolean marketingEmailsEnabled;

    @NotNull private Boolean twoFactorEnabled;

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

  public static class PhoneNumber {

    @NotNull private Type type;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String number;

    public Type getType() {
      return type;
    }

    public void setType(Type type) {
      this.type = type;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

    public enum Type {
      MOBILE,
      HOME,
      WORK,
      OTHER
    }
  }
}
