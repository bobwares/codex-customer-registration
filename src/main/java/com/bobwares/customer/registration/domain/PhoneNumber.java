package com.bobwares.customer.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class PhoneNumber {

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type", nullable = false)
    private PhoneType type;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_number", nullable = false)
    private String number;

    public PhoneNumber() {
    }

    public PhoneNumber(PhoneType type, String number) {
        this.type = type;
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
