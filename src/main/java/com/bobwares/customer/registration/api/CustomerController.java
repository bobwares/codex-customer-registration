/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD operations for customers.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerEmail;
import com.bobwares.customer.registration.CustomerPhoneNumber;
import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.PostalAddress;
import com.bobwares.customer.registration.PrivacySettings;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create customer")
    public ResponseEntity<CustomerDto.Response> create(@Valid @RequestBody CustomerDto.CreateRequest request) {
        Customer saved = service.create(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    public CustomerDto.Response get(@PathVariable UUID id) {
        return toResponse(service.get(id));
    }

    @GetMapping
    @Operation(summary = "List customers")
    public List<CustomerDto.Response> list() {
        return service.list().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public CustomerDto.Response update(@PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
        Customer updated = service.update(id, toEntity(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete customer")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    private Customer toEntity(CustomerDto.CreateRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setMiddleName(request.middleName());
        customer.setLastName(request.lastName());
        if (request.address() != null) {
            PostalAddress address = new PostalAddress();
            address.setLine1(request.address().line1());
            address.setLine2(request.address().line2());
            address.setCity(request.address().city());
            address.setState(request.address().state());
            address.setPostalCode(request.address().postalCode());
            address.setCountry(request.address().country());
            customer.setAddress(address);
        }
        PrivacySettings ps = new PrivacySettings();
        ps.setMarketingEmailsEnabled(request.privacySettings().marketingEmailsEnabled());
        ps.setTwoFactorEnabled(request.privacySettings().twoFactorEnabled());
        customer.setPrivacySettings(ps);
        request.emails().forEach(e -> {
            CustomerEmail ce = new CustomerEmail();
            ce.setCustomer(customer);
            ce.setEmail(e);
            customer.getEmails().add(ce);
        });
        request.phoneNumbers().forEach(p -> {
            CustomerPhoneNumber pn = new CustomerPhoneNumber();
            pn.setCustomer(customer);
            pn.setType(p.type());
            pn.setNumber(p.number());
            customer.getPhoneNumbers().add(pn);
        });
        return customer;
    }

    private Customer toEntity(CustomerDto.UpdateRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setMiddleName(request.middleName());
        customer.setLastName(request.lastName());
        if (request.address() != null) {
            PostalAddress address = new PostalAddress();
            address.setLine1(request.address().line1());
            address.setLine2(request.address().line2());
            address.setCity(request.address().city());
            address.setState(request.address().state());
            address.setPostalCode(request.address().postalCode());
            address.setCountry(request.address().country());
            customer.setAddress(address);
        }
        PrivacySettings ps = new PrivacySettings();
        ps.setMarketingEmailsEnabled(request.privacySettings().marketingEmailsEnabled());
        ps.setTwoFactorEnabled(request.privacySettings().twoFactorEnabled());
        customer.setPrivacySettings(ps);
        request.emails().forEach(e -> {
            CustomerEmail ce = new CustomerEmail();
            ce.setCustomer(customer);
            ce.setEmail(e);
            customer.getEmails().add(ce);
        });
        request.phoneNumbers().forEach(p -> {
            CustomerPhoneNumber pn = new CustomerPhoneNumber();
            pn.setCustomer(customer);
            pn.setType(p.type());
            pn.setNumber(p.number());
            customer.getPhoneNumbers().add(pn);
        });
        return customer;
    }

    private CustomerDto.Response toResponse(Customer customer) {
        List<String> emails = customer.getEmails().stream().map(CustomerEmail::getEmail).collect(Collectors.toList());
        List<CustomerDto.PhoneNumberDto> phones = customer.getPhoneNumbers().stream()
                .map(p -> new CustomerDto.PhoneNumberDto(p.getType(), p.getNumber()))
                .collect(Collectors.toList());
        CustomerDto.PostalAddressDto addressDto = null;
        if (customer.getAddress() != null) {
            addressDto = new CustomerDto.PostalAddressDto(
                    customer.getAddress().getLine1(),
                    customer.getAddress().getLine2(),
                    customer.getAddress().getCity(),
                    customer.getAddress().getState(),
                    customer.getAddress().getPostalCode(),
                    customer.getAddress().getCountry());
        }
        CustomerDto.PrivacySettingsDto psDto = new CustomerDto.PrivacySettingsDto(
                customer.getPrivacySettings().getMarketingEmailsEnabled(),
                customer.getPrivacySettings().getTwoFactorEnabled());
        return new CustomerDto.Response(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                emails,
                phones,
                addressDto,
                psDto);
    }
}
