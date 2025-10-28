package com.bobwares.customer.registration.mapper;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.dto.PhoneNumberDto;
import com.bobwares.customer.registration.dto.PostalAddressDto;
import com.bobwares.customer.registration.dto.PrivacySettingsDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.model.PhoneNumber;
import com.bobwares.customer.registration.model.PostalAddress;
import com.bobwares.customer.registration.model.PrivacySettings;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        updateEntity(customer, request);
        return customer;
    }

    public void updateEntity(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setMiddleName(request.middleName());
        customer.setLastName(request.lastName());
        customer.setEmails(toEmailSet(request.emails()));
        customer.setPhoneNumbers(toPhoneNumberEntities(request.phoneNumbers()));
        customer.setAddress(toPostalAddress(request.address()));
        customer.setPrivacySettings(toPrivacySettings(request.privacySettings()));
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                new ArrayList<>(customer.getEmails()),
                toPhoneNumberDtos(customer.getPhoneNumbers()),
                toPostalAddressDto(customer.getAddress()),
                toPrivacySettingsDto(customer.getPrivacySettings())
        );
    }

    private Set<String> toEmailSet(List<String> emails) {
        if (emails == null) {
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(emails);
    }

    private List<PhoneNumber> toPhoneNumberEntities(List<PhoneNumberDto> phoneNumbers) {
        List<PhoneNumber> results = new ArrayList<>();
        if (phoneNumbers == null) {
            return results;
        }
        for (PhoneNumberDto dto : phoneNumbers) {
            if (dto == null) {
                continue;
            }
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setType(dto.type().toLowerCase());
            phoneNumber.setNumber(dto.number());
            results.add(phoneNumber);
        }
        return results;
    }

    private List<PhoneNumberDto> toPhoneNumberDtos(List<PhoneNumber> phoneNumbers) {
        List<PhoneNumberDto> results = new ArrayList<>();
        if (phoneNumbers == null) {
            return results;
        }
        for (PhoneNumber phoneNumber : phoneNumbers) {
            if (phoneNumber == null) {
                continue;
            }
            results.add(new PhoneNumberDto(phoneNumber.getType(), phoneNumber.getNumber()));
        }
        return results;
    }

    private PostalAddress toPostalAddress(PostalAddressDto dto) {
        if (dto == null) {
            return null;
        }
        PostalAddress address = new PostalAddress();
        address.setLine1(dto.line1());
        address.setLine2(dto.line2());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());
        return address;
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
        PrivacySettings privacySettings = new PrivacySettings();
        privacySettings.setMarketingEmailsEnabled(Boolean.TRUE.equals(dto.marketingEmailsEnabled()));
        privacySettings.setTwoFactorEnabled(Boolean.TRUE.equals(dto.twoFactorEnabled()));
        return privacySettings;
    }

    private PrivacySettingsDto toPrivacySettingsDto(PrivacySettings entity) {
        if (entity == null) {
            return null;
        }
        return new PrivacySettingsDto(entity.isMarketingEmailsEnabled(), entity.isTwoFactorEnabled());
    }
}
