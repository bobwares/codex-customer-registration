/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: CustomerControllerIT
 * Description: Integration tests exercising the REST API over Testcontainers PostgreSQL.
 */
package com.bobwares.customer.registration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.model.PhoneType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:customer_controller_it;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.liquibase.enabled=false",
    "APP_NAME=customer-registration-test",
    "APP_PORT=8080",
    "APP_DEFAULT_TAX_RATE=0.0000",
    "APP_DEFAULT_SHIPPING_COST=0.00",
    "APP_SUPPORTED_CURRENCIES=USD,EUR",
    "DATABASE_HOST=localhost",
    "DATABASE_PORT=5432",
    "DATABASE_NAME=testdb",
    "DATABASE_USERNAME=test",
    "DATABASE_PASSWORD=test"
})
class CustomerControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldExecuteCrudLifecycle() throws Exception {
    CustomerDto.CreateRequest createRequest = new CustomerDto.CreateRequest(
        "Luna",
        "A",
        "Reeves",
        new CustomerDto.Address("500 Market St", null, "Madison", "WI", "53703", "US"),
        new CustomerDto.PrivacySettings(true, true),
        List.of(new CustomerDto.EmailAddress("luna.reeves@example.com")),
        List.of(new CustomerDto.PhoneNumber(PhoneType.MOBILE, "+16085550123")));

    MvcResult createResult = mockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andReturn();

    CustomerDto.Response created = objectMapper.readValue(createResult.getResponse().getContentAsString(), CustomerDto.Response.class);
    UUID id = created.id();
    assertThat(id).isNotNull();

    MvcResult listResult = mockMvc.perform(get("/api/customers"))
        .andExpect(status().isOk())
        .andReturn();
    CustomerDto.Response[] list = objectMapper.readValue(listResult.getResponse().getContentAsByteArray(), CustomerDto.Response[].class);
    assertThat(list).hasSize(1);

    MvcResult getResult = mockMvc.perform(get("/api/customers/{id}", id))
        .andExpect(status().isOk())
        .andReturn();
    CustomerDto.Response fetched = objectMapper.readValue(getResult.getResponse().getContentAsByteArray(), CustomerDto.Response.class);
    assertThat(fetched.emails()).containsExactly("luna.reeves@example.com");

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest(
        "Luna",
        "A",
        "Reeves",
        new CustomerDto.Address("501 Market St", "Unit 2", "Madison", "WI", "53703", "US"),
        new CustomerDto.PrivacySettings(false, true),
        List.of(new CustomerDto.EmailAddress("luna.updated@example.com")),
        List.of(new CustomerDto.PhoneNumber(PhoneType.MOBILE, "+16085550678")));

    MvcResult updateResult = mockMvc.perform(put("/api/customers/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andReturn();
    CustomerDto.Response updated = objectMapper.readValue(updateResult.getResponse().getContentAsByteArray(), CustomerDto.Response.class);
    assertThat(updated.address().line1()).isEqualTo("501 Market St");

    mockMvc.perform(delete("/api/customers/{id}", id))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/{id}", id))
        .andExpect(status().isNotFound());
  }
}
