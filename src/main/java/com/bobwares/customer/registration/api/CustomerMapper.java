/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerMapper.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerMapper
 * Description: Maps between customer DTO payloads and persistence entities.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.PhoneNumber;
import com.bobwares.customer.registration.PostalAddress;
import com.bobwares.customer.registration.PrivacySettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CustomerMapper {

  private CustomerMapper() {}

  public static Customer toEntity(CustomerDto.CreateRequest request) {
    Customer customer = new Customer();
    apply(request, customer);
    return customer;
  }

  public static Customer toEntity(CustomerDto.CreateRequest request, List<String> emails) {
    Customer customer = new Customer();
    apply(request, customer, emails);
    return customer;
  }

  public static void apply(CustomerDto.CreateRequest request, Customer customer) {
    apply(request, customer, normalizeEmails(request.getEmails()));
  }

  public static void apply(CustomerDto.CreateRequest request, Customer customer, List<String> emails) {
    customer.setFirstName(request.getFirstName());
    customer.setMiddleName(request.getMiddleName());
    customer.setLastName(request.getLastName());
    customer.setEmails(new ArrayList<>(emails));
    customer.setPhoneNumbers(
        request.getPhoneNumbers().stream().map(CustomerMapper::toPhoneNumber).collect(Collectors.toList()));
    customer.setAddress(toPostalAddress(request.getAddress()));
    customer.setPrivacySettings(toPrivacySettings(request.getPrivacySettings()));
  }

  public static CustomerDto.Response toResponse(Customer customer) {
    CustomerDto.Response response = new CustomerDto.Response();
    response.setId(customer.getId());
    response.setFirstName(customer.getFirstName());
    response.setMiddleName(customer.getMiddleName());
    response.setLastName(customer.getLastName());
    response.setEmails(new ArrayList<>(customer.getEmails()));
    response.setPhoneNumbers(
        customer.getPhoneNumbers().stream()
            .map(CustomerMapper::toPhoneNumberPayload)
            .collect(Collectors.toList()));
    response.setAddress(toPostalAddressPayload(customer.getAddress()));
    response.setPrivacySettings(toPrivacySettingsPayload(customer.getPrivacySettings()));
    return response;
  }

  private static PhoneNumber toPhoneNumber(CustomerDto.PhoneNumberPayload payload) {
    PhoneNumber phoneNumber = new PhoneNumber();
    phoneNumber.setType(payload.getType());
    phoneNumber.setNumber(payload.getNumber());
    return phoneNumber;
  }

  private static CustomerDto.PhoneNumberPayload toPhoneNumberPayload(PhoneNumber phoneNumber) {
    CustomerDto.PhoneNumberPayload payload = new CustomerDto.PhoneNumberPayload();
    payload.setType(phoneNumber.getType());
    payload.setNumber(phoneNumber.getNumber());
    return payload;
  }

  private static PostalAddress toPostalAddress(CustomerDto.PostalAddressPayload payload) {
    if (payload == null) {
      return null;
    }
    PostalAddress address = new PostalAddress();
    address.setLine1(payload.getLine1());
    address.setLine2(payload.getLine2());
    address.setCity(payload.getCity());
    address.setState(payload.getState());
    address.setPostalCode(payload.getPostalCode());
    address.setCountry(payload.getCountry());
    return address;
  }

  private static CustomerDto.PostalAddressPayload toPostalAddressPayload(PostalAddress address) {
    if (address == null) {
      return null;
    }
    CustomerDto.PostalAddressPayload payload = new CustomerDto.PostalAddressPayload();
    payload.setLine1(address.getLine1());
    payload.setLine2(address.getLine2());
    payload.setCity(address.getCity());
    payload.setState(address.getState());
    payload.setPostalCode(address.getPostalCode());
    payload.setCountry(address.getCountry());
    return payload;
  }

  private static PrivacySettings toPrivacySettings(CustomerDto.PrivacySettingsPayload payload) {
    if (payload == null) {
      return null;
    }
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(payload.getMarketingEmailsEnabled());
    settings.setTwoFactorEnabled(payload.getTwoFactorEnabled());
    return settings;
  }

  private static CustomerDto.PrivacySettingsPayload toPrivacySettingsPayload(PrivacySettings settings) {
    if (settings == null) {
      return null;
    }
    CustomerDto.PrivacySettingsPayload payload = new CustomerDto.PrivacySettingsPayload();
    payload.setMarketingEmailsEnabled(settings.getMarketingEmailsEnabled());
    payload.setTwoFactorEnabled(settings.getTwoFactorEnabled());
    return payload;
  }

  public static List<String> normalizeEmails(List<String> emails) {
    Objects.requireNonNull(emails, "Emails collection must not be null");

    List<String> normalized = new ArrayList<>(emails.size());
    for (String email : emails) {
      if (email == null) {
        throw new IllegalArgumentException("Email address must not be null");
      }
      String trimmed = email.trim();
      if (trimmed.isEmpty()) {
        throw new IllegalArgumentException("Email address must not be blank");
      }
      normalized.add(trimmed);
    }
    return normalized;
  }

  public static void ensureUniqueEmails(List<String> emails) {
    long uniqueCount = emails.stream().map(String::toLowerCase).distinct().count();
    if (emails.size() != uniqueCount) {
      throw new IllegalArgumentException("Duplicate email addresses are not allowed");
    }
  }
}
