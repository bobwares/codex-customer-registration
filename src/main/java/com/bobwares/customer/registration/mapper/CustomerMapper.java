package com.bobwares.customer.registration.mapper;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.domain.PhoneNumber;
import com.bobwares.customer.registration.domain.PostalAddress;
import com.bobwares.customer.registration.domain.PrivacySettings;
import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.dto.PhoneNumberDto;
import com.bobwares.customer.registration.dto.PostalAddressDto;
import com.bobwares.customer.registration.dto.PrivacySettingsDto;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toNewEntity(CustomerRequest request) {
        return toEntity(request, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
    }

    public Customer toExistingEntity(CustomerRequest request, Customer existing) {
        existing.setFirstName(request.getFirstName());
        existing.setMiddleName(request.getMiddleName());
        existing.setLastName(request.getLastName());
        existing.setEmails(new LinkedHashSet<>(request.getEmails()));
        existing.setPhoneNumbers(toPhoneNumbers(request.getPhoneNumbers()));
        existing.setAddress(toPostalAddress(request.getAddress()));
        existing.setPrivacySettings(toPrivacySettings(request.getPrivacySettings()));
        existing.setUpdatedAt(OffsetDateTime.now());
        return existing;
    }

    private Customer toEntity(CustomerRequest request, UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        return new Customer(
                id,
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                new LinkedHashSet<>(request.getEmails()),
                toPhoneNumbers(request.getPhoneNumbers()),
                toPostalAddress(request.getAddress()),
                toPrivacySettings(request.getPrivacySettings()),
                createdAt,
                updatedAt
        );
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                List.copyOf(customer.getEmails()),
                toPhoneNumberDtos(customer.getPhoneNumbers()),
                toPostalAddressDto(customer.getAddress()),
                toPrivacySettingsDto(customer.getPrivacySettings()),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    private Set<PhoneNumber> toPhoneNumbers(List<PhoneNumberDto> phoneNumberDtos) {
        if (phoneNumberDtos == null) {
            return Set.of();
        }
        return phoneNumberDtos.stream()
                .map(dto -> new PhoneNumber(dto.getType(), dto.getNumber()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private List<PhoneNumberDto> toPhoneNumberDtos(Set<PhoneNumber> phoneNumbers) {
        return phoneNumbers == null ? List.of() : phoneNumbers.stream()
                .map(number -> new PhoneNumberDto(number.getType(), number.getNumber()))
                .toList();
    }

    private PostalAddress toPostalAddress(PostalAddressDto dto) {
        if (dto == null) {
            return null;
        }
        return new PostalAddress(
                dto.getLine1(),
                dto.getLine2(),
                dto.getCity(),
                dto.getState(),
                dto.getPostalCode(),
                dto.getCountry()
        );
    }

    private PostalAddressDto toPostalAddressDto(PostalAddress address) {
        if (address == null) {
            return null;
        }
        return new PostalAddressDto(
                address.getLine1(),
                address.getLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    private PrivacySettings toPrivacySettings(PrivacySettingsDto dto) {
        if (dto == null) {
            return null;
        }
        return new PrivacySettings(
                dto.getMarketingEmailsEnabled(),
                dto.getTwoFactorEnabled()
        );
    }

    private PrivacySettingsDto toPrivacySettingsDto(PrivacySettings privacySettings) {
        if (privacySettings == null) {
            return null;
        }
        return new PrivacySettingsDto(
                privacySettings.isMarketingEmailsEnabled(),
                privacySettings.isTwoFactorEnabled()
        );
    }
}
