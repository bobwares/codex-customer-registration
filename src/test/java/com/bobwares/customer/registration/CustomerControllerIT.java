/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: CustomerControllerIT
 * Description: Integration test exercising customer REST endpoints end-to-end using Testcontainers Postgres.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerIT {

  @Container
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerCrudFlow() throws Exception {
    String createPayload =
        objectMapper.writeValueAsString(
            Map.of(
                "firstName",
                "Alice",
                "lastName",
                "Smith",
                "emails",
                new String[] {"alice@example.com"},
                "phoneNumbers",
                new Object[] {
                  Map.of("type", "MOBILE", "number", "+12025550123")
                },
                "address",
                Map.of(
                    "line1", "123 Main",
                    "city", "City",
                    "state", "ST",
                    "postalCode", "12345",
                    "country", "US"),
                "privacySettings",
                Map.of("marketingEmailsEnabled", true, "twoFactorEnabled", true)));

    MvcResult createResult =
        mockMvc
            .perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(createPayload))
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode createdNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
    UUID id = UUID.fromString(createdNode.get("id").asText());
    assertThat(createdNode.get("emails").get(0).asText()).isEqualTo("alice@example.com");

    mockMvc.perform(get("/api/customers/{id}", id)).andExpect(status().isOk());

    String updatePayload =
        objectMapper.writeValueAsString(
            Map.of(
                "firstName",
                "Bob",
                "lastName",
                "Jones",
                "emails",
                new String[] {"bob@example.com"},
                "phoneNumbers",
                new Object[] {Map.of("type", "HOME", "number", "+12025550124")},
                "address",
                Map.of(
                    "line1", "456 Elm",
                    "line2", "Suite 2",
                    "city", "Town",
                    "state", "TS",
                    "postalCode", "67890",
                    "country", "US"),
                "privacySettings",
                Map.of("marketingEmailsEnabled", false, "twoFactorEnabled", true)));

    MvcResult updateResult =
        mockMvc
            .perform(
                put("/api/customers/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatePayload))
            .andExpect(status().isOk())
            .andReturn();

    JsonNode updatedNode = objectMapper.readTree(updateResult.getResponse().getContentAsString());
    assertThat(updatedNode.get("firstName").asText()).isEqualTo("Bob");

    mockMvc.perform(delete("/api/customers/{id}", id)).andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/{id}", id)).andExpect(status().isNotFound());
  }
}
