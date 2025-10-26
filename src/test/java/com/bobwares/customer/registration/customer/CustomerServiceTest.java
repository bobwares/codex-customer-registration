/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer
 * File: CustomerServiceTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerServiceTest
 * Description: Validates CustomerService CRUD operations and uniqueness rules using the in-memory test profile.
 */
package com.bobwares.customer.registration.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bobwares.customer.registration.customer.model.Customer;
import com.bobwares.customer.registration.customer.model.CustomerEmail;
import com.bobwares.customer.registration.customer.model.CustomerPhoneNumber;
import com.bobwares.customer.registration.customer.model.PhoneType;
import com.bobwares.customer.registration.customer.model.PostalAddress;
import com.bobwares.customer.registration.customer.model.PrivacySettings;
import com.bobwares.customer.registration.customer.service.CustomerService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @AfterEach
    void cleanUp() {
        service.list().forEach(customer -> service.delete(customer.getId()));
    }

    @Test
    void createCustomerPersistsAggregate() {
        Customer created = service.create(sampleCustomer("ada@example.com"));

        Customer loaded = service.get(created.getId());
        assertThat(loaded.getEmails()).hasSize(1);
        assertThat(loaded.getPrivacySettings().isMarketingEmailsEnabled()).isTrue();
    }

    @Test
    void duplicateEmailThrowsIllegalArgumentException() {
        service.create(sampleCustomer("ada@example.com"));

        assertThatThrownBy(() -> service.create(sampleCustomer("ada@example.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ada@example.com");
    }

    @Test
    void updateCustomerReplacesAggregateState() {
        Customer created = service.create(sampleCustomer("ada@example.com"));
        UUID id = created.getId();

        Customer update = sampleCustomer("ada.byron@example.com");
        update.setId(id);
        update.getPrivacySettings().setMarketingEmailsEnabled(false);

        Customer result = service.update(id, update);
        assertThat(result.getEmails()).extracting(CustomerEmail::getEmail)
                .containsExactly("ada.byron@example.com");
        assertThat(result.getPrivacySettings().isMarketingEmailsEnabled()).isFalse();
    }

    @Test
    void deleteCustomerRemovesAggregate() {
        Customer created = service.create(sampleCustomer("ada@example.com"));
        UUID id = created.getId();

        service.delete(id);

        assertThatThrownBy(() -> service.get(id))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    private Customer sampleCustomer(String email) {
        Customer customer = new Customer();
        customer.setFirstName("Ada");
        customer.setMiddleName("M");
        customer.setLastName("Lovelace");

        PostalAddress address = new PostalAddress();
        address.setLine1("10 Downing St");
        address.setCity("London");
        address.setState("LDN");
        address.setPostalCode("SW1A 2AA");
        address.setCountry("GB");
        customer.setAddress(address);

        PrivacySettings privacy = new PrivacySettings();
        privacy.setMarketingEmailsEnabled(true);
        privacy.setTwoFactorEnabled(true);
        customer.setPrivacySettings(privacy);

        CustomerEmail customerEmail = new CustomerEmail();
        customerEmail.setEmail(email);
        customer.addEmail(customerEmail);

        CustomerPhoneNumber phoneNumber = new CustomerPhoneNumber();
        phoneNumber.setPhoneType(PhoneType.MOBILE);
        phoneNumber.setPhoneNumber("+441234567890");
        customer.addPhoneNumber(phoneNumber);

        return customer;
    }
}
