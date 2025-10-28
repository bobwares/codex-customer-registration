package com.bobwares.customer.registration.web;

import com.bobwares.customer.registration.repository.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchCustomer() throws Exception {
        String payload = """
                {
                  \"firstName\": \"Jane\",
                  \"middleName\": \"Q\",
                  \"lastName\": \"Public\",
                  \"emails\": [\"jane.public@example.com\"],
                  \"phoneNumbers\": [
                    {"type": "MOBILE", "number": "+15551234567"}
                  ],
                  \"address\": {
                    \"line1\": \"123 Main St\",
                    \"city\": \"Metropolis\",
                    \"state\": \"NY\",
                    \"postalCode\": \"12345\",
                    \"country\": \"US\"
                  },
                  \"privacySettings\": {
                    \"marketingEmailsEnabled\": true,
                    \"twoFactorEnabled\": true
                  }
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID id = UUID.fromString(responseBody.get("id").asText());

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.emails[0]").value("jane.public@example.com"));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()));

        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidPayload() throws Exception {
        String payload = """
                {
                  \"firstName\": \"\",
                  \"lastName\": \"\",
                  \"emails\": []
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForUnknownCustomer() throws Exception {
        mockMvc.perform(get("/api/customers/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
