/**
 * App: Customer Registration Package: com.bobwares.customer.registration.service File:
 * CustomerServiceTests.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date:
 * 2025-10-30T06:53:03Z Exports: CustomerServiceTests Description: Exercises customer service CRUD
 * flows and uniqueness validation against PostgreSQL Testcontainers.
 */
package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bobwares.customer.registration.AbstractIntegrationTest;
import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerDto.Address;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumber;
import com.bobwares.customer.registration.api.CustomerDto.Privacy;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTests extends AbstractIntegrationTest {

  @Autowired private CustomerService service;

  @Autowired private CustomerRepository repository;

  @BeforeEach
  void clean() {
    repository.deleteAll();
  }

  @Test
  void createCustomerPersistsAggregate() {
    CustomerDto.CreateRequest request = sampleCreateRequest("test@example.com");

    CustomerDto.Response response = service.create(request);

    assertThat(response.getId()).isNotNull();
    assertThat(response.getEmails()).containsExactly("test@example.com");
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void createCustomerRejectsDuplicateEmail() {
    CustomerDto.CreateRequest request = sampleCreateRequest("dup@example.com");
    service.create(request);

    assertThatThrownBy(() -> service.create(sampleCreateRequest("dup@example.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("dup@example.com");
  }

  @Test
  void updateCustomerMutatesExistingRecord() {
    CustomerDto.Response created = service.create(sampleCreateRequest("update@example.com"));

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest();
    updateRequest.setFirstName("Allison");
    updateRequest.setMiddleName("B.");
    updateRequest.setLastName("Walker");
    updateRequest.setAddress(sampleAddress());
    updateRequest.setPrivacy(samplePrivacy());
    updateRequest.setEmails(List.of("update@example.com"));
    updateRequest.setPhoneNumbers(List.of(samplePhone()));

    CustomerDto.Response updated = service.update(created.getId(), updateRequest);

    assertThat(updated.getFirstName()).isEqualTo("Allison");
    assertThat(updated.getPhoneNumbers()).hasSize(1);
  }

  @Test
  void deleteCustomerRemovesRecord() {
    CustomerDto.Response created = service.create(sampleCreateRequest("delete@example.com"));

    service.delete(created.getId());

    assertThat(repository.existsById(created.getId())).isFalse();
  }

  private CustomerDto.CreateRequest sampleCreateRequest(String email) {
    CustomerDto.CreateRequest request = new CustomerDto.CreateRequest();
    request.setFirstName("Alice");
    request.setMiddleName("Q.");
    request.setLastName("Smith");
    request.setAddress(sampleAddress());
    request.setPrivacy(samplePrivacy());
    request.setEmails(List.of(email));
    request.setPhoneNumbers(List.of(samplePhone()));
    return request;
  }

  private Address sampleAddress() {
    Address address = new Address();
    address.setLine1("123 Main St");
    address.setLine2("Apt 4");
    address.setCity("Springfield");
    address.setState("IL");
    address.setPostalCode("62704");
    address.setCountry("US");
    return address;
  }

  private Privacy samplePrivacy() {
    Privacy privacy = new Privacy();
    privacy.setMarketingEmailsEnabled(true);
    privacy.setTwoFactorEnabled(true);
    return privacy;
  }

  private PhoneNumber samplePhone() {
    PhoneNumber phone = new PhoneNumber();
    phone.setType(PhoneNumber.Type.MOBILE);
    phone.setNumber("+15551234567");
    return phone;
  }
}
