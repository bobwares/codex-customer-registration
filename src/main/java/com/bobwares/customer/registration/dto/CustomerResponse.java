package com.bobwares.customer.registration.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class CustomerResponse {

    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
    private List<String> emails;
    private List<PhoneNumberDto> phoneNumbers;
    private PostalAddressDto address;
    private PrivacySettingsDto privacySettings;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CustomerResponse() {
    }

    public CustomerResponse(UUID id,
                            String firstName,
                            String middleName,
                            String lastName,
                            List<String> emails,
                            List<PhoneNumberDto> phoneNumbers,
                            PostalAddressDto address,
                            PrivacySettingsDto privacySettings,
                            OffsetDateTime createdAt,
                            OffsetDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.address = address;
        this.privacySettings = privacySettings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<PhoneNumberDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public PostalAddressDto getAddress() {
        return address;
    }

    public void setAddress(PostalAddressDto address) {
        this.address = address;
    }

    public PrivacySettingsDto getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(PrivacySettingsDto privacySettings) {
        this.privacySettings = privacySettings;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
