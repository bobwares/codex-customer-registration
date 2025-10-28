/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.support
 * File: CustomerMapper.java
 * Version: 0.1.1
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerMapper
 * Description: Utility component that transforms customer DTOs into JPA entities and vice versa.
 */
package com.bobwares.customer.registration.support;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.PhoneNumber;
import com.bobwares.customer.registration.model.PhoneNumberType;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static Customer toEntity(CustomerRequest request) {
        Customer customer = Customer.builder()
            .firstName(request.getFirstName())
            .middleName(request.getMiddleName())
            .lastName(request.getLastName())
            .address(toAddress(request.getAddress()))
            .privacySettings(toPrivacy(request.getPrivacySettings()))
            .build();
        customer.setEmails(new LinkedHashSet<>(request.getEmails()));
        customer.setPhoneNumbers(toPhoneNumbers(request.getPhoneNumbers()));
        return customer;
    }

    public static void updateEntity(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setAddress(toAddress(request.getAddress()));
        customer.setPrivacySettings(toPrivacy(request.getPrivacySettings()));
        customer.setEmails(new LinkedHashSet<>(request.getEmails()));
        customer.setPhoneNumbers(toPhoneNumbers(request.getPhoneNumbers()));
    }

    public static CustomerResponse toResponse(Customer customer) {
        Set<CustomerResponse.PhoneNumberResponse> phoneResponses = customer.getPhoneNumbers().stream()
            .map(phone -> CustomerResponse.PhoneNumberResponse.builder()
                .type(phone.getType().name().toLowerCase(Locale.US))
                .number(phone.getNumber())
                .build())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        CustomerResponse.AddressResponse addressResponse = null;
        if (customer.getAddress() != null) {
            addressResponse = CustomerResponse.AddressResponse.builder()
                .line1(customer.getAddress().getLine1())
                .line2(customer.getAddress().getLine2())
                .city(customer.getAddress().getCity())
                .state(customer.getAddress().getState())
                .postalCode(customer.getAddress().getPostalCode())
                .country(customer.getAddress().getCountry())
                .build();
        }

        CustomerResponse.PrivacySettingsResponse privacyResponse = null;
        if (customer.getPrivacySettings() != null) {
            privacyResponse = CustomerResponse.PrivacySettingsResponse.builder()
                .marketingEmailsEnabled(customer.getPrivacySettings().isMarketingEmailsEnabled())
                .twoFactorEnabled(customer.getPrivacySettings().isTwoFactorEnabled())
                .build();
        }

        return CustomerResponse.of(
            customer.getId(),
            customer.getFirstName(),
            customer.getMiddleName(),
            customer.getLastName(),
            new LinkedHashSet<>(customer.getEmails()),
            phoneResponses,
            addressResponse,
            privacyResponse
        );
    }

    private static PostalAddress toAddress(CustomerRequest.AddressRequest request) {
        if (request == null) {
            return null;
        }
        return PostalAddress.builder()
            .line1(request.getLine1())
            .line2(request.getLine2())
            .city(request.getCity())
            .state(request.getState())
            .postalCode(request.getPostalCode())
            .country(request.getCountry())
            .build();
    }

    private static PrivacySettings toPrivacy(CustomerRequest.PrivacySettingsRequest request) {
        if (request == null) {
            return null;
        }
        return PrivacySettings.builder()
            .marketingEmailsEnabled(request.isMarketingEmailsEnabled())
            .twoFactorEnabled(request.isTwoFactorEnabled())
            .build();
    }

    private static Set<PhoneNumber> toPhoneNumbers(Set<CustomerRequest.PhoneNumberRequest> requestNumbers) {
        if (requestNumbers == null) {
            return new LinkedHashSet<>();
        }
        return requestNumbers.stream()
            .map(number -> PhoneNumber.builder()
                .type(PhoneNumberType.valueOf(number.getType().toUpperCase(Locale.US)))
                .number(number.getNumber())
                .build())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
