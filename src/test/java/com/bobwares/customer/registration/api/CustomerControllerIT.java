/**
 * App: Customer Registration Package: com.bobwares.customer.registration.api File:
 * CustomerControllerIT.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date:
 * 2025-10-30T06:53:03Z Exports: CustomerControllerIT Description: Validates the customer REST API
 * end-to-end using MockMvc and PostgreSQL Testcontainers.
 */
package com.bobwares.customer.registration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerIT extends AbstractIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void fullCrudLifecycleSucceeds() throws Exception {
    CustomerDto.CreateRequest createRequest = buildCreateRequest("lifecycle@example.com");

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn();

    CustomerDto.Response created =
        objectMapper.readValue(
            createResult.getResponse().getContentAsString(), CustomerDto.Response.class);

    MvcResult getResult =
        mockMvc
            .perform(get("/api/customers/{id}", created.getId()))
            .andExpect(status().isOk())
            .andReturn();
    CustomerDto.Response fetched =
        objectMapper.readValue(
            getResult.getResponse().getContentAsString(), CustomerDto.Response.class);
    assertThat(fetched.getEmails()).containsExactly("lifecycle@example.com");

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest();
    updateRequest.setFirstName("Jamie");
    updateRequest.setMiddleName("R.");
    updateRequest.setLastName("Anderson");
    updateRequest.setAddress(createRequest.getAddress());
    updateRequest.setPrivacy(createRequest.getPrivacy());
    updateRequest.setEmails(List.of("lifecycle@example.com", "alt@example.com"));
    updateRequest.setPhoneNumbers(createRequest.getPhoneNumbers());

    mockMvc
        .perform(
            put("/api/customers/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk());

    mockMvc
        .perform(delete("/api/customers/{id}", created.getId()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/{id}", created.getId())).andExpect(status().isNotFound());
  }

  private CustomerDto.CreateRequest buildCreateRequest(String email) {
    CustomerDto.CreateRequest request = new CustomerDto.CreateRequest();
    request.setFirstName("Jamie");
    request.setMiddleName("T.");
    request.setLastName("Anderson");

    CustomerDto.Address address = new CustomerDto.Address();
    address.setLine1("500 Market St");
    address.setLine2("Suite 200");
    address.setCity("San Francisco");
    address.setState("CA");
    address.setPostalCode("94105");
    address.setCountry("US");
    request.setAddress(address);

    CustomerDto.Privacy privacy = new CustomerDto.Privacy();
    privacy.setMarketingEmailsEnabled(true);
    privacy.setTwoFactorEnabled(true);
    request.setPrivacy(privacy);

    CustomerDto.PhoneNumber phoneNumber = new CustomerDto.PhoneNumber();
    phoneNumber.setType(CustomerDto.PhoneNumber.Type.MOBILE);
    phoneNumber.setNumber("+14155550123");
    request.setPhoneNumbers(List.of(phoneNumber));

    request.setEmails(List.of(email));
    return request;
  }
}
