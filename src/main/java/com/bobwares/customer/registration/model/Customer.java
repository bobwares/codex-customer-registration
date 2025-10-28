package com.bobwares.customer.registration.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "customer_emails", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "email", nullable = false)
    private Set<String> emails = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "customer_phone_numbers", joinColumns = @JoinColumn(name = "customer_id"))
    @OrderColumn(name = "order_index")
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @Embedded
    private PostalAddress address;

    @Embedded
    private PrivacySettings privacySettings;

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

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public PostalAddress getAddress() {
        return address;
    }

    public void setAddress(PostalAddress address) {
        this.address = address;
    }

    public PrivacySettings getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(PrivacySettings privacySettings) {
        this.privacySettings = privacySettings;
    }
}
