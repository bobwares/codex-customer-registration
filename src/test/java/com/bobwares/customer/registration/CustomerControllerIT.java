/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-14T03:38:27Z
 * Exports: none
 * Description: Integration tests for CustomerController using Testcontainers PostgreSQL.
 */
package com.bobwares.customer.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bobwares.customer.registration.api.CustomerDto;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void fullCrudFlow() throws Exception {
        CustomerDto.CreateRequest req = new CustomerDto.CreateRequest();
        req.setFirstName("Eve");
        req.setLastName("Adams");
        req.setEmails(List.of("eve@example.com"));
        CustomerDto.PrivacySettingsDto ps = new CustomerDto.PrivacySettingsDto();
        ps.setMarketingEmailsEnabled(true);
        ps.setTwoFactorEnabled(false);
        req.setPrivacySettings(ps);

        String createJson = objectMapper.writeValueAsString(req);
        String location = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Eve"));

        req.setFirstName("Evelyn");
        String updateJson = objectMapper.writeValueAsString(req);
        mockMvc.perform(put(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Evelyn"));

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }
}
