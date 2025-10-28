package com.bobwares.customer.registration.web.dto;

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

    public CustomerResponse() {
    }

    public CustomerResponse(UUID id, String firstName, String middleName, String lastName, List<String> emails,
                            List<PhoneNumberDto> phoneNumbers, PostalAddressDto address, PrivacySettingsDto privacySettings) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.address = address;
        this.privacySettings = privacySettings;
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
}
