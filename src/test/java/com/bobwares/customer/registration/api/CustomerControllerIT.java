/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: CustomerControllerIT
 * Description: Integration test covering the full REST lifecycle using Testcontainers-backed PostgreSQL.
 */
package com.bobwares.customer.registration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CustomerControllerIT {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16").withDatabaseName("customer_registration");

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.liquibase.enabled", () -> "false");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    registry.add("spring.jpa.properties.hibernate.default_schema", () -> "customer_registration");
    registry.add("spring.jpa.properties.hibernate.hbm2ddl.create_namespaces", () -> "true");
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerLifecycle() throws Exception {
    String createPayload =
        objectMapper.writeValueAsString(
            Map.of(
                "firstName", "Alice",
                "middleName", "Q",
                "lastName", "Smith",
                "privacySettings",
                    Map.of("marketingEmailsEnabled", true, "twoFactorEnabled", true),
                "address",
                    Map.of(
                        "line1", "123 Main",
                        "line2", "",
                        "city", "Springfield",
                        "state", "IL",
                        "postalCode", "62701",
                        "country", "US"),
                "emails", java.util.List.of("alice@example.com"),
                "phoneNumbers",
                    java.util.List.of(
                        Map.of("type", "MOBILE", "number", "+15551234567"))));

    String createResponse =
        mockMvc
            .perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(createPayload))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JsonNode createdNode = objectMapper.readTree(createResponse);
    String id = createdNode.get("id").asText();

    mockMvc.perform(get("/api/customers")).andExpect(status().isOk());

    mockMvc.perform(get("/api/customers/" + id)).andExpect(status().isOk());

    String updatePayload =
        objectMapper.writeValueAsString(
            Map.of(
                "firstName", "Alicia",
                "middleName", "Q",
                "lastName", "Smith",
                "privacySettings",
                    Map.of("marketingEmailsEnabled", false, "twoFactorEnabled", true),
                "address",
                    Map.of(
                        "line1", "456 Elm",
                        "line2", "",
                        "city", "Springfield",
                        "state", "IL",
                        "postalCode", "62702",
                        "country", "US"),
                "emails", java.util.List.of("alice@example.com"),
                "phoneNumbers",
                    java.util.List.of(
                        Map.of("type", "HOME", "number", "+15557654321"))));

    mockMvc
        .perform(
            put("/api/customers/" + id).contentType(MediaType.APPLICATION_JSON).content(updatePayload))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/customers/" + id)).andExpect(status().isNoContent());

    String deletedResponse =
        mockMvc
            .perform(get("/api/customers/" + id))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JsonNode error = objectMapper.readTree(deletedResponse);
    assertThat(error.get("status").asInt()).isEqualTo(404);
  }
}
