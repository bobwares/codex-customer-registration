package com.bobwares.customer.registration.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CustomerRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotEmpty
    private List<@Email String> emails;

    @Valid
    private List<@NotNull PhoneNumberDto> phoneNumbers;

    @Valid
    private PostalAddressDto address;

    @Valid
    @NotNull
    private PrivacySettingsDto privacySettings;

    public CustomerRequest() {
    }

    public CustomerRequest(String firstName,
                           String middleName,
                           String lastName,
                           List<String> emails,
                           List<PhoneNumberDto> phoneNumbers,
                           PostalAddressDto address,
                           PrivacySettingsDto privacySettings) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.address = address;
        this.privacySettings = privacySettings;
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
