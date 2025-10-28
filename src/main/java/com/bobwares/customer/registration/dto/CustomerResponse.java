/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.dto
 * File: CustomerResponse.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerResponse, AddressResponse, PhoneNumberResponse, PrivacySettingsResponse
 * Description: Response payload representing persisted customer details returned by the REST API.
 */
package com.bobwares.customer.registration.dto;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {

    private final UUID id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final Set<String> emails;
    private final Set<PhoneNumberResponse> phoneNumbers;
    private final AddressResponse address;
    private final PrivacySettingsResponse privacySettings;

    @Getter
    @Builder
    public static class AddressResponse {
        private final String line1;
        private final String line2;
        private final String city;
        private final String state;
        private final String postalCode;
        private final String country;
    }

    @Getter
    @Builder
    public static class PhoneNumberResponse {
        private final String type;
        private final String number;
    }

    @Getter
    @Builder
    public static class PrivacySettingsResponse {
        private final boolean marketingEmailsEnabled;
        private final boolean twoFactorEnabled;
    }

    public static CustomerResponse of(UUID id,
                                      String firstName,
                                      String middleName,
                                      String lastName,
                                      Set<String> emails,
                                      Set<PhoneNumberResponse> phoneNumbers,
                                      AddressResponse address,
                                      PrivacySettingsResponse privacySettings) {
        return CustomerResponse.builder()
            .id(id)
            .firstName(firstName)
            .middleName(middleName)
            .lastName(lastName)
            .emails(new LinkedHashSet<>(emails))
            .phoneNumbers(new LinkedHashSet<>(phoneNumbers))
            .address(address)
            .privacySettings(privacySettings)
            .build();
    }
}
