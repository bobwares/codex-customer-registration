package com.bobwares.customer.registration.mapper;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.domain.PhoneNumber;
import com.bobwares.customer.registration.domain.PostalAddress;
import com.bobwares.customer.registration.domain.PrivacySettings;
import com.bobwares.customer.registration.web.dto.CustomerRequest;
import com.bobwares.customer.registration.web.dto.CustomerResponse;
import com.bobwares.customer.registration.web.dto.PhoneNumberDto;
import com.bobwares.customer.registration.web.dto.PostalAddressDto;
import com.bobwares.customer.registration.web.dto.PrivacySettingsDto;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        if (request.getEmails() != null) {
            customer.setEmails(new LinkedHashSet<>(request.getEmails()));
        }
        customer.setPhoneNumbers(toPhoneNumberSet(request.getPhoneNumbers()));
        customer.setAddress(toPostalAddress(request.getAddress()));
        customer.setPrivacySettings(toPrivacySettings(request.getPrivacySettings()));
        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                customer.getEmails() == null ? List.of() : List.copyOf(customer.getEmails()),
                toPhoneNumberDtos(customer.getPhoneNumbers()),
                toPostalAddressDto(customer.getAddress()),
                toPrivacySettingsDto(customer.getPrivacySettings())
        );
    }

    private Set<PhoneNumber> toPhoneNumberSet(List<PhoneNumberDto> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return new LinkedHashSet<>();
        }
        Set<PhoneNumber> result = new LinkedHashSet<>();
        for (PhoneNumberDto dto : phoneNumbers) {
            if (dto != null) {
                result.add(new PhoneNumber(dto.getType(), dto.getNumber()));
            }
        }
        return result;
    }

    private List<PhoneNumberDto> toPhoneNumberDtos(Set<PhoneNumber> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return List.of();
        }
        return phoneNumbers.stream()
                .filter(Objects::nonNull)
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
        return new PrivacySettings(dto.getMarketingEmailsEnabled(), dto.getTwoFactorEnabled());
    }

    private PrivacySettingsDto toPrivacySettingsDto(PrivacySettings settings) {
        if (settings == null) {
            return null;
        }
        return new PrivacySettingsDto(settings.isMarketingEmailsEnabled(), settings.isTwoFactorEnabled());
    }
}
