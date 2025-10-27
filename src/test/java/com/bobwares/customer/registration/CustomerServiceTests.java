/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: CustomerServiceTests
 * Description: Unit tests covering CustomerService validation and repository interactions.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bobwares.customer.registration.api.CustomerDto;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerService service;

  private CustomerDto.CreateRequest request;

  @BeforeEach
  void setUp() {
    request = new CustomerDto.CreateRequest();
    request.setFirstName("Ada");
    request.setLastName("Lovelace");
    request.setMiddleName("Grace");
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
  }

  @Test
  void createPersistsCustomerWhenUnique() {
    Customer saved = new Customer();
    saved.setId(UUID.randomUUID());
    saved.setFirstName("Ada");
    saved.setLastName("Lovelace");
    saved.setEmails(List.of("ada@example.com"));

    request.setEmails(List.of(" ada@example.com "));

    doReturn(false).when(repository).existsByEmail("ada@example.com");
    doReturn(saved).when(repository).save(any(Customer.class));

    CustomerDto.Response response = service.create(request);

    assertThat(response.getId()).isEqualTo(saved.getId());
    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getEmails()).containsExactly("ada@example.com");
  }

  @Test
  void createRejectsDuplicateEmailsWithinRequest() {
    request.setEmails(List.of("dup@example.com", "dup@example.com"));

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Duplicate email addresses");
  }

  @Test
  void createRejectsExistingEmail() {
    doReturn(true).when(repository).existsByEmail("ada@example.com");

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already registered");
  }

  @Test
  void createRejectsNullEmails() {
    request.setEmails(List.of("ada@example.com", null));

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  void createRejectsBlankEmails() {
    request.setEmails(List.of("   "));

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be blank");
  }

  @Test
  void createRejectsDuplicateEmailsIgnoringWhitespaceAndCase() {
    request.setEmails(List.of("Ada@example.com", " ada@example.com "));

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Duplicate email");
  }

  @Test
  void updateEnforcesMatchingIdentifiers() {
    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest();
    updateRequest.setId(UUID.randomUUID());
    updateRequest.setFirstName(request.getFirstName());
    updateRequest.setLastName(request.getLastName());
    updateRequest.setMiddleName(request.getMiddleName());
    updateRequest.setEmails(request.getEmails());
    updateRequest.setPhoneNumbers(request.getPhoneNumbers());
    updateRequest.setAddress(request.getAddress());
    updateRequest.setPrivacySettings(request.getPrivacySettings());

    assertThatThrownBy(() -> service.update(UUID.randomUUID(), updateRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Path id");
  }
}
