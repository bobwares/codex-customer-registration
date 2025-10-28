package com.bobwares.customer.registration.web.dto;

import com.bobwares.customer.registration.domain.PhoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PhoneNumberDto {

    @NotNull
    private PhoneType type;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String number;

    public PhoneNumberDto() {
    }

    public PhoneNumberDto(PhoneType type, String number) {
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
