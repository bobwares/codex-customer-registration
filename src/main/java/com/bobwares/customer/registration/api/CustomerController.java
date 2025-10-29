/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-29T05:30:00Z
 * Exports: CustomerController
 * Description: REST controller delivering CRUD endpoints for customers with OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerService;
import com.bobwares.customer.registration.PhoneNumber;
import com.bobwares.customer.registration.PrivacySettings;
import com.bobwares.customer.registration.PostalAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/customers")
@Validated
@Tag(name = "Customers", description = "Customer registration CRUD API")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(
      summary = "Create customer",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer created",
            content = @Content(schema = @Schema(implementation = CustomerDto.Response.class)))
      })
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request, UriComponentsBuilder uriBuilder) {
    Customer saved = service.create(request);
    URI location = uriBuilder.path("/api/customers/{id}").buildAndExpand(saved.getId()).toUri();
    return ResponseEntity.created(location).body(toResponse(saved));
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get customer",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer fetched",
            content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<CustomerDto.Response> get(@PathVariable UUID id) {
    return ResponseEntity.ok(toResponse(service.get(id)));
  }

  @GetMapping
  @Operation(
      summary = "List customers",
      responses =
          @ApiResponse(
              responseCode = "200",
              description = "Customer list",
              content = @Content(schema = @Schema(implementation = CustomerDto.Summary.class))))
  public ResponseEntity<List<CustomerDto.Summary>> list() {
    List<CustomerDto.Summary> items = service.list().stream().map(this::toSummary).toList();
    return ResponseEntity.ok(items);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update customer", responses = @ApiResponse(responseCode = "200", description = "Customer updated"))
  public ResponseEntity<CustomerDto.Response> update(
      @PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
    Customer updated = service.update(id, request);
    return ResponseEntity.ok(toResponse(updated));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete customer", responses = @ApiResponse(responseCode = "204", description = "Customer deleted"))
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  private CustomerDto.Response toResponse(Customer customer) {
    PostalAddress address = customer.getAddress();
    PrivacySettings privacy = customer.getPrivacySettings();
    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        customer.getPrimaryEmail(),
        customer.getEmails(),
        mapPhoneNumbers(customer.getPhoneNumbers()),
        new CustomerDto.PostalAddressRequest(
            address.getLine1(),
            address.getLine2(),
            address.getCity(),
            address.getState(),
            address.getPostalCode(),
            address.getCountry()),
        new CustomerDto.PrivacySettingsRequest(
            privacy.getMarketingEmailsEnabled(), privacy.getTwoFactorEnabled()),
        customer.getCreatedAt(),
        customer.getUpdatedAt());
  }

  private CustomerDto.Summary toSummary(Customer customer) {
    return new CustomerDto.Summary(
        customer.getId(),
        customer.getFirstName(),
        customer.getLastName(),
        customer.getPrimaryEmail(),
        customer.getCreatedAt(),
        customer.getUpdatedAt());
  }

  private List<CustomerDto.PhoneNumberRequest> mapPhoneNumbers(Set<PhoneNumber> numbers) {
    return numbers.stream()
        .map(number -> new CustomerDto.PhoneNumberRequest(number.getType(), number.getNumber()))
        .collect(Collectors.toList());
  }
}
