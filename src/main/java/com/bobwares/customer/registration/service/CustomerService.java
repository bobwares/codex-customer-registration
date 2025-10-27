package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.domain.Customer;
import com.bobwares.customer.registration.domain.PhoneNumber;
import com.bobwares.customer.registration.domain.PostalAddress;
import com.bobwares.customer.registration.domain.PrivacySettings;
import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.dto.PhoneNumberRequest;
import com.bobwares.customer.registration.dto.PhoneNumberResponse;
import com.bobwares.customer.registration.dto.PostalAddressRequest;
import com.bobwares.customer.registration.dto.PostalAddressResponse;
import com.bobwares.customer.registration.dto.PrivacySettingsRequest;
import com.bobwares.customer.registration.dto.PrivacySettingsResponse;
import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public CustomerResponse create(CustomerRequest request) {
        Customer customer = new Customer();
        updateEntityFromRequest(customer, request);
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        updateEntityFromRequest(customer, request);
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    private void updateEntityFromRequest(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setMiddleName(request.middleName());
        customer.setLastName(request.lastName());
        customer.setEmails(mapEmails(request.emails()));
        customer.setPhoneNumbers(mapPhoneNumbers(request.phoneNumbers()));
        customer.setAddress(mapAddress(request.address()));
        customer.setPrivacySettings(mapPrivacySettings(request.privacySettings()));
    }

    private List<String> mapEmails(List<String> emails) {
        return emails.stream()
                .collect(Collectors.toList());
    }

    private List<PhoneNumber> mapPhoneNumbers(List<PhoneNumberRequest> phoneNumbers) {
        return phoneNumbers.stream()
                .map(request -> {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setType(request.type());
                    phoneNumber.setNumber(request.number());
                    return phoneNumber;
                })
                .collect(Collectors.toList());
    }

    private PostalAddress mapAddress(PostalAddressRequest request) {
        if (request == null) {
            return null;
        }
        PostalAddress address = new PostalAddress();
        address.setLine1(request.line1());
        address.setLine2(request.line2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostalCode(request.postalCode());
        address.setCountry(request.country());
        return address;
    }

    private PrivacySettings mapPrivacySettings(PrivacySettingsRequest request) {
        PrivacySettings settings = new PrivacySettings();
        settings.setMarketingEmailsEnabled(Boolean.TRUE.equals(request.marketingEmailsEnabled()));
        settings.setTwoFactorEnabled(Boolean.TRUE.equals(request.twoFactorEnabled()));
        return settings;
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                List.copyOf(customer.getEmails()),
                customer.getPhoneNumbers().stream()
                        .map(phone -> new PhoneNumberResponse(phone.getType(), phone.getNumber()))
                        .toList(),
                mapAddressResponse(customer.getAddress()),
                mapPrivacySettingsResponse(customer.getPrivacySettings())
        );
    }

    private PostalAddressResponse mapAddressResponse(PostalAddress address) {
        if (address == null) {
            return null;
        }
        return new PostalAddressResponse(
                address.getLine1(),
                address.getLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    private PrivacySettingsResponse mapPrivacySettingsResponse(PrivacySettings privacySettings) {
        if (privacySettings == null) {
            return null;
        }
        return new PrivacySettingsResponse(
                privacySettings.isMarketingEmailsEnabled(),
                privacySettings.isTwoFactorEnabled()
        );
    }
}
