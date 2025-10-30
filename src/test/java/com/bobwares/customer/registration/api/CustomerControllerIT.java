/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: CustomerControllerIT
 * Description: Exercises the REST API end-to-end against PostgreSQL via MockMvc and Testcontainers.
 */
package com.bobwares.customer.registration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bobwares.customer.registration.support.PostgresContainerSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT extends PostgresContainerSupport {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void createReadUpdateDeleteFlow() throws Exception {
    CustomerDto.CreateRequest createRequest =
        new CustomerDto.CreateRequest(
            "Alice",
            "Q",
            "Example",
            List.of("alice@example.com"),
            List.of(new CustomerDto.PhoneNumberDto("mobile", "+18005550101")),
            new CustomerDto.AddressDto("123 Market", "Suite 100", "San Francisco", "CA", "94105", "US"),
            new CustomerDto.PrivacySettingsDto(true, true));

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("Alice"))
            .andReturn();

    CustomerDto.Response created =
        objectMapper.readValue(createResult.getResponse().getContentAsString(), CustomerDto.Response.class);

    mockMvc
        .perform(get("/api/customers/{id}", created.id()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.emails[0]").value("alice@example.com"));

    CustomerDto.UpdateRequest updateRequest =
        new CustomerDto.UpdateRequest(
            "Alicia",
            "Q",
            "Example",
            List.of("alice@example.com"),
            List.of(new CustomerDto.PhoneNumberDto("home", "+18005550102")),
            new CustomerDto.AddressDto("500 Howard", null, "San Francisco", "CA", "94105", "US"),
            new CustomerDto.PrivacySettingsDto(false, true));

    mockMvc
        .perform(
            put("/api/customers/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Alicia"));

    mockMvc.perform(delete("/api/customers/{id}", created.id())).andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/{id}", created.id())).andExpect(status().isNotFound());
  }
}
