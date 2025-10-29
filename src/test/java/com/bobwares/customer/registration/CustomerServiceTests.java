/**
 * App: Customer Registration Package: com.bobwares.customer.registration File:
 * CustomerServiceTests.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T19:49:40Z Exports: CustomerServiceTests Description: Unit tests verifying
 * service-layer validation, mapping, and repository interactions for customers.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bobwares.customer.registration.api.CustomerDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerService service;

  private CustomerDto.CreateRequest createRequest;

  @BeforeEach
  void setUp() {
    createRequest =
        CustomerDto.CreateRequest.builder()
            .firstName("Ada")
            .lastName("Lovelace")
            .emails(List.of("ada@example.com"))
            .privacySettings(
                CustomerDto.PrivacySettings.builder()
                    .marketingEmailsEnabled(Boolean.TRUE)
                    .twoFactorEnabled(Boolean.TRUE)
                    .build())
            .build();
  }

  @Test
  void createCustomerPersistsEntity() {
    Customer saved = new Customer();
    saved.setFirstName("Ada");
    saved.setLastName("Lovelace");
    saved.setMarketingEmailsEnabled(true);
    saved.setTwoFactorEnabled(true);
    when(repository.save(any(Customer.class))).thenReturn(saved);

    CustomerDto.Response response = service.create(createRequest);

    assertThat(response.getFirstName()).isEqualTo("Ada");
    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getEmails()).hasSize(1);
  }

  @Test
  void createCustomerRejectsDuplicateEmails() {
    when(repository.existsByEmailsEmailIgnoreCase("ada@example.com")).thenReturn(true);

    assertThatThrownBy(() -> service.create(createRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already registered");
  }

  @Test
  void updateCustomerReplacesEmails() {
    UUID id = UUID.randomUUID();
    Customer existing = new Customer();
    existing.setMarketingEmailsEnabled(true);
    existing.setTwoFactorEnabled(true);
    when(repository.findById(id)).thenReturn(Optional.of(existing));

    CustomerDto.UpdateRequest updateRequest =
        CustomerDto.UpdateRequest.builder()
            .firstName("Ada")
            .lastName("Byron")
            .emails(List.of("ada@update.com"))
            .privacySettings(
                CustomerDto.PrivacySettings.builder()
                    .marketingEmailsEnabled(Boolean.FALSE)
                    .twoFactorEnabled(Boolean.TRUE)
                    .build())
            .build();

    CustomerDto.Response response = service.update(id, updateRequest);

    assertThat(response.getEmails()).containsExactly("ada@update.com");
    verify(repository, times(0)).save(existing);
  }

  @Test
  void getMissingCustomerThrows() {
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(id))
        .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
        .hasMessageContaining("Customer not found");
  }
}
