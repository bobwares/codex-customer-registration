/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.api
 * File: CustomerMapper.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerMapper
 * Description: Utility methods converting between customer DTOs and domain entities.
 */
package com.bobwares.customer.registration.customer.api;

import com.bobwares.customer.registration.customer.model.Customer;
import com.bobwares.customer.registration.customer.model.CustomerEmail;
import com.bobwares.customer.registration.customer.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.customer.model.PhoneType;
import com.bobwares.customer.registration.customer.model.PostalAddress;
import com.bobwares.customer.registration.customer.model.PrivacySettings;
import java.util.List;

final class CustomerMapper {

    private CustomerMapper() {
    }

    static Customer toEntity(CustomerDto.CreateRequest request) {
        Customer customer = new Customer();
        populate(request.firstName(), request.middleName(), request.lastName(),
                request.emails(), request.address(), request.privacySettings(), request.phoneNumbers(), customer);
        return customer;
    }

    static Customer toEntity(CustomerDto.UpdateRequest request) {
        Customer customer = new Customer();
        customer.setId(request.id());
        populate(request.firstName(), request.middleName(), request.lastName(),
                request.emails(), request.address(), request.privacySettings(), request.phoneNumbers(), customer);
        return customer;
    }

    static CustomerDto.Response toResponse(Customer customer) {
        List<String> emails = customer.getEmails().stream()
                .map(CustomerEmail::getEmail)
                .toList();

        List<CustomerDto.PhoneNumber> phoneNumbers = customer.getPhoneNumbers().stream()
                .map(number -> new CustomerDto.PhoneNumber(number.getPhoneType(), number.getPhoneNumber()))
                .toList();

        CustomerDto.Address addressDto = null;
        if (customer.getAddress() != null) {
            var address = customer.getAddress();
            addressDto = new CustomerDto.Address(
                    address.getLine1(),
                    address.getLine2(),
                    address.getCity(),
                    address.getState(),
                    address.getPostalCode(),
                    address.getCountry()
            );
        }

        var privacy = customer.getPrivacySettings();
        var privacyDto = new CustomerDto.PrivacySettings(
                privacy.isMarketingEmailsEnabled(),
                privacy.isTwoFactorEnabled());

        return new CustomerDto.Response(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                emails,
                addressDto,
                privacyDto,
                phoneNumbers
        );
    }

    private static void populate(String firstName,
                                  String middleName,
                                  String lastName,
                                  List<String> emails,
                                  CustomerDto.Address addressDto,
                                  CustomerDto.PrivacySettings privacyDto,
                                  List<CustomerDto.PhoneNumber> phoneNumbers,
                                  Customer target) {
        target.setFirstName(firstName);
        target.setMiddleName(middleName);
        target.setLastName(lastName);

        if (addressDto != null) {
            PostalAddress address = new PostalAddress();
            address.setLine1(addressDto.line1());
            address.setLine2(addressDto.line2());
            address.setCity(addressDto.city());
            address.setState(addressDto.state());
            address.setPostalCode(addressDto.postalCode());
            address.setCountry(addressDto.country());
            target.setAddress(address);
        } else {
            target.setAddress(null);
        }

        PrivacySettings privacySettings = new PrivacySettings();
        privacySettings.setMarketingEmailsEnabled(privacyDto.marketingEmailsEnabled());
        privacySettings.setTwoFactorEnabled(privacyDto.twoFactorEnabled());
        target.setPrivacySettings(privacySettings);

        target.clearEmails();
        for (String email : emails) {
            CustomerEmail entity = new CustomerEmail();
            entity.setEmail(email);
            target.addEmail(entity);
        }

        target.clearPhoneNumbers();
        if (phoneNumbers != null) {
            for (CustomerDto.PhoneNumber number : phoneNumbers) {
                CustomerPhoneNumber entity = new CustomerPhoneNumber();
                entity.setPhoneNumber(number.number());
                entity.setPhoneType(number.type() == null ? PhoneType.MOBILE : number.type());
                target.addPhoneNumber(entity);
            }
        }
    }
}
