/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: CreateRequest, UpdateRequest, Response
 * Description: Data transfer objects for Customer API.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostalAddressDto {
        @NotBlank @Size(max = 255) private String line1;
        @Size(max = 255) private String line2;
        @NotBlank @Size(max = 100) private String city;
        @NotBlank @Size(max = 50) private String state;
        @Size(max = 20) private String postalCode;
        @NotBlank @Size(min = 2, max = 2) private String country;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PhoneNumberDto {
        @NotNull private PhoneType type;
        @NotBlank @Size(max = 15) private String number;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PrivacySettingsDto {
        @NotNull private Boolean marketingEmailsEnabled;
        @NotNull private Boolean twoFactorEnabled;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotBlank @Size(max = 255) private String firstName;
        @Size(max = 255) private String middleName;
        @NotBlank @Size(max = 255) private String lastName;
        @NotNull @Size(min = 1) private List<@Email @Size(max = 255) String> emails;
        private List<@Valid PhoneNumberDto> phoneNumbers;
        private @Valid PostalAddressDto address;
        @NotNull @Valid private PrivacySettingsDto privacySettings;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateRequest extends CreateRequest {
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String firstName;
        private String middleName;
        private String lastName;
        private List<String> emails;
        private List<PhoneNumberDto> phoneNumbers;
        private PostalAddressDto address;
        private PrivacySettingsDto privacySettings;
    }

    public static Customer toEntity(CreateRequest dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setLastName(dto.getLastName());

        if (dto.getAddress() != null) {
            PostalAddress address = new PostalAddress();
            address.setLine1(dto.getAddress().getLine1());
            address.setLine2(dto.getAddress().getLine2());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setPostalCode(dto.getAddress().getPostalCode());
            address.setCountry(dto.getAddress().getCountry());
            customer.setAddress(address);
        }

        PrivacySettings ps = new PrivacySettings();
        ps.setMarketingEmailsEnabled(dto.getPrivacySettings().getMarketingEmailsEnabled());
        ps.setTwoFactorEnabled(dto.getPrivacySettings().getTwoFactorEnabled());
        customer.setPrivacySettings(ps);

        dto.getEmails().forEach(email -> {
            CustomerEmail ce = new CustomerEmail();
            ce.setEmail(email);
            ce.setCustomer(customer);
            customer.getEmails().add(ce);
        });

        if (dto.getPhoneNumbers() != null) {
            dto.getPhoneNumbers().forEach(p -> {
                CustomerPhoneNumber pn = new CustomerPhoneNumber();
                pn.setType(p.getType());
                pn.setNumber(p.getNumber());
                pn.setCustomer(customer);
                customer.getPhoneNumbers().add(pn);
            });
        }
        return customer;
    }

    public static Response fromEntity(Customer customer) {
        Response resp = new Response();
        resp.setId(customer.getId().toString());
        resp.setFirstName(customer.getFirstName());
        resp.setMiddleName(customer.getMiddleName());
        resp.setLastName(customer.getLastName());
        resp.setEmails(customer.getEmails().stream().map(CustomerEmail::getEmail).collect(Collectors.toList()));
        List<PhoneNumberDto> phones = customer.getPhoneNumbers().stream().map(p -> {
            PhoneNumberDto dto = new PhoneNumberDto();
            dto.setType(p.getType());
            dto.setNumber(p.getNumber());
            return dto;
        }).collect(Collectors.toList());
        resp.setPhoneNumbers(phones);
        if (customer.getAddress() != null) {
            PostalAddressDto ad = new PostalAddressDto();
            ad.setLine1(customer.getAddress().getLine1());
            ad.setLine2(customer.getAddress().getLine2());
            ad.setCity(customer.getAddress().getCity());
            ad.setState(customer.getAddress().getState());
            ad.setPostalCode(customer.getAddress().getPostalCode());
            ad.setCountry(customer.getAddress().getCountry());
            resp.setAddress(ad);
        }
        PrivacySettingsDto ps = new PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(customer.getPrivacySettings().getMarketingEmailsEnabled());
        ps.setTwoFactorEnabled(customer.getPrivacySettings().getTwoFactorEnabled());
        resp.setPrivacySettings(ps);
        return resp;
    }
}
