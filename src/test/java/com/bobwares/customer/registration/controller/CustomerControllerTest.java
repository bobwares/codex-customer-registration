package com.bobwares.customer.registration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.PhoneNumberRequest;
import com.bobwares.customer.registration.dto.PostalAddressRequest;
import com.bobwares.customer.registration.dto.PrivacySettingsRequest;
import com.bobwares.customer.registration.repository.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void createAndRetrieveCustomer() throws Exception {
        CustomerRequest request = sampleRequest();

        String payload = objectMapper.writeValueAsString(request);

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.emails[0]").value("jane.doe@example.com"))
                .andReturn();

        JsonNode createBody = objectMapper.readTree(createResult.getResponse().getContentAsString());
        UUID id = UUID.fromString(createBody.get("id").asText());

        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.privacySettings.marketingEmailsEnabled").value(true));
    }

    @Test
    void updateCustomer() throws Exception {
        UUID id = createCustomer();

        CustomerRequest updateRequest = new CustomerRequest(
                "Janet",
                "Q",
                "Doe",
                List.of("janet.doe@example.com"),
                List.of(new PhoneNumberRequest("mobile", "+12223334444")),
                new PostalAddressRequest("456 Elm St", null, "Metropolis", "NY", "10001", "US"),
                new PrivacySettingsRequest(false, true)
        );

        mockMvc.perform(put("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Janet"))
                .andExpect(jsonPath("$.emails[0]").value("janet.doe@example.com"))
                .andExpect(jsonPath("$.privacySettings.twoFactorEnabled").value(true));
    }

    @Test
    void deleteCustomerRemovesRecord() throws Exception {
        UUID id = createCustomer();

        mockMvc.perform(delete("/api/customers/" + id))
                .andExpect(status().isNoContent());

        assertThat(customerRepository.existsById(id)).isFalse();
    }

    @Test
    void getUnknownCustomerReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/customers/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    private UUID createCustomer() throws Exception {
        CustomerRequest request = sampleRequest();
        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(body.get("id").asText());
    }

    private CustomerRequest sampleRequest() {
        return new CustomerRequest(
                "Jane",
                "Q",
                "Doe",
                List.of("jane.doe@example.com"),
                List.of(new PhoneNumberRequest("mobile", "+12345678901")),
                new PostalAddressRequest("123 Main St", "Apt 4", "Springfield", "IL", "62704", "US"),
                new PrivacySettingsRequest(true, false)
        );
    }
}
