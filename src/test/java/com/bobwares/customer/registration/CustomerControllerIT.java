/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * CustomerControllerIT.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T19:49:40Z Exports: CustomerControllerIT Description: Integration test executing
 * a full CRUD flow against the REST API with Testcontainers-backed PostgreSQL.
 */
package com.bobwares.customer.registration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.api.CustomerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CustomerControllerIT {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("DATABASE_HOST", postgres::getHost);
    registry.add("DATABASE_PORT", () -> String.valueOf(postgres.getMappedPort(5432)));
    registry.add("DATABASE_NAME", postgres::getDatabaseName);
    registry.add("DATABASE_USERNAME", postgres::getUsername);
    registry.add("DATABASE_PASSWORD", postgres::getPassword);
    registry.add("APP_NAME", () -> "customer-registration");
    registry.add("APP_PORT", () -> "8080");
    registry.add("APP_DEFAULT_TAX_RATE", () -> "0.00");
    registry.add("APP_DEFAULT_SHIPPING_COST", () -> "0.00");
    registry.add("APP_SUPPORTED_CURRENCIES", () -> "USD");
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerCrudFlow() throws Exception {
    CustomerDto.CreateRequest createRequest =
        CustomerDto.CreateRequest.builder()
            .firstName("Grace")
            .lastName("Hopper")
            .emails(List.of("grace@example.com"))
            .privacySettings(
                CustomerDto.PrivacySettings.builder()
                    .marketingEmailsEnabled(Boolean.TRUE)
                    .twoFactorEnabled(Boolean.TRUE)
                    .build())
            .build();

    String location =
        mockMvc
            .perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn()
            .getResponse()
            .getHeader("Location");

    mockMvc.perform(get(location)).andExpect(status().isOk());

    CustomerDto.UpdateRequest updateRequest =
        CustomerDto.UpdateRequest.builder()
            .firstName("Grace")
            .lastName("Murray")
            .emails(List.of("grace@example.com", "g.murray@example.com"))
            .privacySettings(
                CustomerDto.PrivacySettings.builder()
                    .marketingEmailsEnabled(Boolean.FALSE)
                    .twoFactorEnabled(Boolean.TRUE)
                    .build())
            .build();

    mockMvc
        .perform(
            put(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk());

    mockMvc.perform(delete(location)).andExpect(status().isNoContent());

    mockMvc.perform(get(location)).andExpect(status().isNotFound());
  }
}
