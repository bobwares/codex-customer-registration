/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.customer.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-26T00:40:26Z
 * Exports: CustomerControllerIT
 * Description: Integration tests for the customer REST API using MockMvc and the shared test profile.
 */
package com.bobwares.customer.registration.customer.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createPayload;

    @BeforeEach
    void setUp() throws Exception {
        createPayload = objectMapper.writeValueAsString(Map.of(
                "firstName", "Ada",
                "middleName", "M",
                "lastName", "Lovelace",
                "emails", new String[]{"ada.integration@example.com"},
                "address", Map.of(
                        "line1", "10 Downing St",
                        "line2", "",
                        "city", "London",
                        "state", "LDN",
                        "postalCode", "SW1A 2AA",
                        "country", "GB"
                ),
                "privacySettings", Map.of(
                        "marketingEmailsEnabled", true,
                        "twoFactorEnabled", true
                ),
                "phoneNumbers", new Object[]{Map.of(
                        "type", "MOBILE",
                        "number", "+441234567890"
                )}
        ));
    }

    @Test
    void fullCrudLifecycle() throws Exception {
        String createResponse = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.emails", hasSize(1)))
                .andReturn().getResponse().getContentAsString();

        JsonNode node = objectMapper.readTree(createResponse);
        String id = node.get("id").asText();

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Ada")));

        String updatePayload = objectMapper.writeValueAsString(Map.of(
                "id", id,
                "firstName", "Ada",
                "middleName", "M",
                "lastName", "Byron",
                "emails", new String[]{"ada.byron.integration@example.com"},
                "address", Map.of(
                        "line1", "10 Downing St",
                        "line2", "",
                        "city", "London",
                        "state", "LDN",
                        "postalCode", "SW1A 2AA",
                        "country", "GB"
                ),
                "privacySettings", Map.of(
                        "marketingEmailsEnabled", false,
                        "twoFactorEnabled", true
                ),
                "phoneNumbers", new Object[]{Map.of(
                        "type", "MOBILE",
                        "number", "+441234567890"
                )}
        ));

        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", equalTo("Byron")))
                .andExpect(jsonPath("$.privacySettings.marketingEmailsEnabled", equalTo(false)));

        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isNotFound());
    }
}
