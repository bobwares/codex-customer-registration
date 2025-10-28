package com.bobwares.customer.registration.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.dto.PhoneNumberDto;
import com.bobwares.customer.registration.dto.PostalAddressDto;
import com.bobwares.customer.registration.dto.PrivacySettingsDto;
import com.bobwares.customer.registration.exception.GlobalExceptionHandler;
import com.bobwares.customer.registration.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CustomerController.class)
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void createCustomerReturnsValidationErrors() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "",
                null,
                "",
                List.of("invalid-email"),
                List.of(new PhoneNumberDto("mobile", "12345")),
                new PostalAddressDto("", null, "", "", "", "USA"),
                new PrivacySettingsDto(null, null)
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.firstName").exists());
    }

    @Test
    void getCustomerReturnsPayload() throws Exception {
        UUID id = UUID.randomUUID();
        CustomerResponse response = new CustomerResponse(
                id,
                "Taylor",
                "Q",
                "Swift",
                List.of("taylor@example.com"),
                List.of(new PhoneNumberDto("mobile", "+14155550123")),
                new PostalAddressDto("1 Music Way", null, "Nashville", "TN", "37203", "US"),
                new PrivacySettingsDto(true, true)
        );
        when(customerService.getCustomer(id)).thenReturn(response);

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Taylor"));
    }
}
