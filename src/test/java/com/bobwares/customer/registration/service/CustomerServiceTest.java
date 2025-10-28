package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bobwares.customer.registration.dto.CustomerRequest;
import com.bobwares.customer.registration.dto.CustomerResponse;
import com.bobwares.customer.registration.dto.PhoneNumberDto;
import com.bobwares.customer.registration.dto.PostalAddressDto;
import com.bobwares.customer.registration.dto.PrivacySettingsDto;
import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomerPersistsEntity() {
        CustomerRequest request = buildRequest("Alice", "Johnson");

        CustomerResponse response = customerService.createCustomer(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.firstName()).isEqualTo("Alice");
        assertThat(customerRepository.count()).isEqualTo(1);
    }

    @Test
    void getCustomerReturnsStoredData() {
        CustomerResponse created = customerService.createCustomer(buildRequest("Bob", "Smith"));

        CustomerResponse loaded = customerService.getCustomer(created.id());

        assertThat(loaded.firstName()).isEqualTo("Bob");
        assertThat(loaded.address().city()).isEqualTo("Springfield");
    }

    @Test
    void updateCustomerReplacesMutableFields() {
        CustomerResponse created = customerService.createCustomer(buildRequest("Carol", "Baker"));

        CustomerRequest updateRequest = new CustomerRequest(
                "Caroline",
                "Anne",
                "Baker",
                List.of("caroline.baker@example.com"),
                List.of(new PhoneNumberDto("work", "+14155552671")),
                new PostalAddressDto("200 Market St", null, "Portland", "OR", "97201", "US"),
                new PrivacySettingsDto(false, true)
        );

        CustomerResponse updated = customerService.updateCustomer(created.id(), updateRequest);

        assertThat(updated.firstName()).isEqualTo("Caroline");
        assertThat(updated.phoneNumbers()).hasSize(1);
        assertThat(updated.phoneNumbers().get(0).number()).isEqualTo("+14155552671");
    }

    @Test
    void deleteCustomerRemovesRecord() {
        CustomerResponse created = customerService.createCustomer(buildRequest("Dana", "Reed"));

        customerService.deleteCustomer(created.id());

        assertThat(customerRepository.existsById(created.id())).isFalse();
    }

    @Test
    void getCustomerThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> customerService.getCustomer(id))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    private CustomerRequest buildRequest(String firstName, String lastName) {
        return new CustomerRequest(
                firstName,
                null,
                lastName,
                List.of("test@example.com"),
                List.of(new PhoneNumberDto("mobile", "+14155552671")),
                new PostalAddressDto("100 Main St", "Suite 1", "Springfield", "IL", "62701", "US"),
                new PrivacySettingsDto(true, true)
        );
    }
}
