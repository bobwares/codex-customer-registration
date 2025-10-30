/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.1
 * Turns: Turn 1, Turn 2
 * Author: Bobwares
 * Date: 2025-10-30T08:28:00Z
 * Exports: CustomerControllerIT
 * Description: Integration test validating the full REST lifecycle against PostgreSQL via Testcontainers.
 */
package com.bobwares.customer.registration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
@EnabledIf("dockerAvailable")
class CustomerControllerIT {

  private static final PostgreSQLContainer<?> postgres = startPostgres();

  private static PostgreSQLContainer<?> startPostgres() {
    if (dockerAvailable()) {
      try {
        PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("customer_reg")
                .withUsername("test")
                .withPassword("test");
        container.start();
        return container;
      } catch (Throwable ex) {
        return null;
      }
    }
    return null;
  }

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry registry) {
    if (postgres != null) {
      registry.add("spring.datasource.url", postgres::getJdbcUrl);
      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
    }
  }

  @AfterAll
  static void tearDown() {
    if (postgres != null) {
      postgres.stop();
    }
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerCrudFlow() throws Exception {
    String createPayload = objectMapper.writeValueAsString(sampleRequest());

    String postResponse =
        mockMvc
            .perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(createPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    JsonNode createdNode = objectMapper.readTree(postResponse);
    UUID id = UUID.fromString(createdNode.get("id").asText());

    mockMvc
        .perform(get("/api/customers/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));

    Map<String, Object> updatePayload = sampleRequest();
    updatePayload.put("lastName", "Doe-Updated");

    mockMvc
        .perform(
            put("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePayload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastName").value("Doe-Updated"));

    mockMvc.perform(delete("/api/customers/{id}", id)).andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/{id}", id)).andExpect(status().isNotFound());
  }

  private Map<String, Object> sampleRequest() {
    Map<String, Object> payload = new java.util.HashMap<>();
    payload.put("firstName", "Jane");
    payload.put("middleName", "Q");
    payload.put("lastName", "Doe");
    payload.put("emails", new String[] {"user@example.com"});
    payload.put(
        "phoneNumbers",
        new Object[] {
          Map.of("type", "MOBILE", "number", "+15551234567")
        });
    payload.put(
        "address",
        Map.of(
            "line1", "123 Main",
            "line2", "",
            "city", "City",
            "state", "State",
            "postalCode", "12345",
            "country", "US"));
    payload.put(
        "privacySettings", Map.of("marketingEmailsEnabled", true, "twoFactorEnabled", true));
    return payload;
  }

  static boolean dockerAvailable() {
    try {
      Path dockerSocket = Path.of("/var/run/docker.sock");
      if (!Files.exists(dockerSocket) && System.getenv("DOCKER_HOST") == null) {
        return false;
      }
      return DockerClientFactory.instance().isDockerAvailable();
    } catch (Throwable ex) {
      return false;
    }
  }
}
