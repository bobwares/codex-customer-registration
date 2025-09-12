/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-09-12T16:28:01Z
 * Exports: none
 * Description: Integration tests for CustomerController.
 */
package com.bobwares.customer.registration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndGet() throws Exception {
        String json = "{" +
                "\"firstName\":\"Bob\"," +
                "\"lastName\":\"Jones\"," +
                "\"emails\":[\"bob@example.com\"]," +
                "\"phoneNumbers\":[{\"type\":\"mobile\",\"number\":\"+15555550111\"}]," +
                "\"address\":{\"line1\":\"123 St\",\"city\":\"City\",\"state\":\"ST\",\"postalCode\":\"12345\",\"country\":\"US\"}," +
                "\"privacySettings\":{\"marketingEmailsEnabled\":true,\"twoFactorEnabled\":false}}";
        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        CustomerDto.Response response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.Response.class);
        mockMvc.perform(get("/api/customers/" + response.id()))
                .andExpect(status().isOk());
    }
}
