package com.bobwares.customer.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PhoneNumber {

    @Column(name = "phone_type")
    private String type;

    @Column(name = "phone_number")
    private String number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
