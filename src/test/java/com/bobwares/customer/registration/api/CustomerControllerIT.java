/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerControllerIT
 * Description: Exercises the REST API end-to-end using an in-memory H2 database configured for PostgreSQL compatibility.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.PhoneNumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "spring.datasource.url=jdbc:h2:mem:customer_controller_it;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
      "spring.liquibase.enabled=false"
    })
@AutoConfigureMockMvc
class CustomerControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void fullCrudLifecycle() throws Exception {
    String createPayload = objectMapper.writeValueAsString(buildCreateRequest());
    MvcResult postResult =
        mockMvc
            .perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(createPayload))
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode createdNode = readBody(postResult);
    UUID id = UUID.fromString(createdNode.get("id").asText());

    mockMvc.perform(get("/api/customers/{id}", id)).andExpect(status().isOk());

    String updatePayload =
        objectMapper.writeValueAsString(
            Map.of(
                "firstName", "Updated",
                "middleName", "Q",
                "lastName", "Customer",
                "emails", List.of("updated@example.com"),
                "address",
                    Map.of(
                        "line1", "123 Updated",
                        "line2", "Suite 5",
                        "city", "City",
                        "state", "State",
                        "postalCode", "12345",
                        "country", "US"),
                "privacySettings",
                    Map.of("marketingEmailsEnabled", true, "twoFactorEnabled", true),
                "phoneNumbers",
                    List.of(Map.of("type", PhoneNumberType.MOBILE.name(), "number", "+18005550100"))));

    mockMvc
        .perform(put("/api/customers/{id}", id).contentType(MediaType.APPLICATION_JSON).content(updatePayload))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/customers/{id}", id)).andExpect(status().isNoContent());
    mockMvc.perform(get("/api/customers/{id}", id)).andExpect(status().isNotFound());
  }

  private Map<String, Object> buildCreateRequest() {
    return Map.of(
        "firstName",
        "Alice",
        "middleName",
        "M",
        "lastName",
        "Example",
        "emails",
        List.of("alice@example.com", "alice.backup@example.com"),
        "address",
        Map.of(
            "line1", "123",
            "line2", "Apt 4",
            "city", "City",
            "state", "State",
            "postalCode", "12345",
            "country", "US"),
        "privacySettings",
        Map.of("marketingEmailsEnabled", true, "twoFactorEnabled", true),
        "phoneNumbers",
        List.of(Map.of("type", PhoneNumberType.MOBILE.name(), "number", "+18005550100")));
  }

  private JsonNode readBody(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return objectMapper.readTree(json);
  }
}
