/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: none
 * Description: Unit tests for CustomerService.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTests {
    @Autowired
    private CustomerService service;

    @Test
    void createAndGetCustomer() {
        Customer c = new Customer();
        c.setFirstName("Alice");
        c.setLastName("Smith");
        PrivacySettings ps = new PrivacySettings();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        c.setPrivacySettings(ps);
        CustomerEmail email = new CustomerEmail();
        email.setCustomer(c);
        email.setEmail("alice@example.com");
        c.getEmails().add(email);
        CustomerPhoneNumber pn = new CustomerPhoneNumber();
        pn.setCustomer(c);
        pn.setType("mobile");
        pn.setNumber("+15555550101");
        c.getPhoneNumbers().add(pn);
        Customer saved = service.create(c);
        assertThat(service.get(saved.getCustomerId()).getFirstName()).isEqualTo("Alice");
    }
}
