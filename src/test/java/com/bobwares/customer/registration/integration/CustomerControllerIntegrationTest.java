/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.integration
 * File: CustomerControllerIntegrationTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerControllerIntegrationTest
 * Description: End-to-end test exercising REST endpoints against a PostgreSQL Testcontainer instance.
 */
package com.bobwares.customer.registration.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.CustomerRegistrationApplication;
import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = CustomerRegistrationApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CustomerControllerIntegrationTest {

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("customer_registration")
        .withUsername("customer")
        .withPassword("customer");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.liquibase.url", POSTGRES::getJdbcUrl);
        registry.add("spring.liquibase.user", POSTGRES::getUsername);
        registry.add("spring.liquibase.password", POSTGRES::getPassword);
    }

    @Test
    void shouldPerformFullCustomerLifecycle() throws Exception {
        CustomerRequest request = buildRequest("Alice", "Smith", "+15551234567");

        String createResponseBody = mockMvc.perform(post("/api/customers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        CustomerResponse created = objectMapper.readValue(createResponseBody, CustomerResponse.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getEmails()).containsExactly("alice@example.com");

        String listResponseBody = mockMvc.perform(get("/api/customers"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        CustomerResponse[] customers = objectMapper.readValue(listResponseBody, CustomerResponse[].class);
        assertThat(customers).hasSize(1);
        assertThat(customers[0].getId()).isEqualTo(created.getId());

        CustomerRequest updateRequest = buildRequest("Alice", "Johnson", "+15557654321");

        String updateResponseBody = mockMvc.perform(put("/api/customers/" + created.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        CustomerResponse updated = objectMapper.readValue(updateResponseBody, CustomerResponse.class);
        assertThat(updated.getLastName()).isEqualTo("Johnson");
        assertThat(updated.getPhoneNumbers()).extracting(CustomerResponse.PhoneNumberResponse::getNumber)
            .containsExactly("+15557654321");

        mockMvc.perform(delete("/api/customers/" + created.getId()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/" + created.getId()))
            .andExpect(status().isNotFound());
    }

    private CustomerRequest buildRequest(String firstName, String lastName, String phoneNumber) {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmails(new LinkedHashSet<>(Set.of("alice@example.com")));

        CustomerRequest.AddressRequest address = new CustomerRequest.AddressRequest();
        address.setLine1("123 Main St");
        address.setCity("Metropolis");
        address.setState("NY");
        address.setPostalCode("12345");
        address.setCountry("US");
        request.setAddress(address);

        CustomerRequest.PhoneNumberRequest phone = new CustomerRequest.PhoneNumberRequest();
        phone.setType("mobile");
        phone.setNumber(phoneNumber);
        request.setPhoneNumbers(new LinkedHashSet<>(Set.of(phone)));

        CustomerRequest.PrivacySettingsRequest privacy = new CustomerRequest.PrivacySettingsRequest();
        privacy.setMarketingEmailsEnabled(true);
        privacy.setTwoFactorEnabled(true);
        request.setPrivacySettings(privacy);

        return request;
    }
}
