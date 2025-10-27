/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerControllerIT
 * Description: Integration test executing end-to-end CRUD operations via the REST API.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.api.CustomerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:integration;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
      "spring.liquibase.enabled=false"
    })
@AutoConfigureMockMvc
class CustomerControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerCrudFlow() throws Exception {
    CustomerDto.CreateRequest createRequest = buildCreateRequest();

    String createResponse =
        mockMvc
            .perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    CustomerDto.Response created =
        objectMapper.readValue(createResponse, CustomerDto.Response.class);

    assertThat(created.getId()).isNotNull();
    assertThat(created.getEmails()).containsExactly("ada@example.com");

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest();
    updateRequest.setId(created.getId());
    updateRequest.setFirstName("Ada");
    updateRequest.setMiddleName("Grace");
    updateRequest.setLastName("Lovelace");
    updateRequest.setEmails(List.of("ada@example.com"));
    updateRequest.setPhoneNumbers(createRequest.getPhoneNumbers());
    CustomerDto.PostalAddressPayload updatedAddress = new CustomerDto.PostalAddressPayload();
    CustomerDto.PostalAddressPayload originalAddress = createRequest.getAddress();
    updatedAddress.setLine1(originalAddress.getLine1());
    updatedAddress.setLine2(originalAddress.getLine2());
    updatedAddress.setCity("Gotham");
    updatedAddress.setState(originalAddress.getState());
    updatedAddress.setPostalCode(originalAddress.getPostalCode());
    updatedAddress.setCountry(originalAddress.getCountry());
    updateRequest.setAddress(updatedAddress);
    updateRequest.setPrivacySettings(createRequest.getPrivacySettings());

    String updateResponse =
        mockMvc
            .perform(
                put("/api/customers/{id}", created.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    CustomerDto.Response updated =
        objectMapper.readValue(updateResponse, CustomerDto.Response.class);
    assertThat(updated.getAddress().getCity()).isEqualTo("Gotham");

    mockMvc
        .perform(get("/api/customers/{id}", created.getId()))
        .andExpect(status().isOk());

    mockMvc
        .perform(delete("/api/customers/{id}", created.getId()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/api/customers/{id}", created.getId()))
        .andExpect(status().isNotFound());
  }

  private CustomerDto.CreateRequest buildCreateRequest() {
    CustomerDto.CreateRequest request = new CustomerDto.CreateRequest();
    request.setFirstName("Ada");
    request.setMiddleName("Grace");
    request.setLastName("Lovelace");
    request.setEmails(List.of("ada@example.com"));

    CustomerDto.PhoneNumberPayload phone = new CustomerDto.PhoneNumberPayload();
    phone.setType(PhoneType.MOBILE);
    phone.setNumber("+15551234567");
    request.setPhoneNumbers(List.of(phone));

    CustomerDto.PostalAddressPayload address = new CustomerDto.PostalAddressPayload();
    address.setLine1("123 Main St");
    address.setCity("Metropolis");
    address.setState("NY");
    address.setPostalCode("10001");
    address.setCountry("US");
    request.setAddress(address);

    CustomerDto.PrivacySettingsPayload privacy = new CustomerDto.PrivacySettingsPayload();
    privacy.setMarketingEmailsEnabled(Boolean.TRUE);
    privacy.setTwoFactorEnabled(Boolean.TRUE);
    request.setPrivacySettings(privacy);

    return request;
  }
}
