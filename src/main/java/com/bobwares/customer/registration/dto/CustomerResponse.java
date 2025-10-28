package com.bobwares.customer.registration.dto;

import java.util.List;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String middleName,
        String lastName,
        List<String> emails,
        List<PhoneNumberDto> phoneNumbers,
        PostalAddressDto address,
        PrivacySettingsDto privacySettings
) {
}
