package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Phone;
import jakarta.validation.constraints.*;

public class PhoneRequest {

    @Digits(integer = 2, fraction = 0, message = "Phone number must be a valid number with up to 2 digits.")
    @NotNull(message = "Required field")
    private Integer ddd;

    @Digits(integer = 9, fraction = 0, message = "Phone number must be a valid number with up to 10 digits.")
    @NotNull(message = "Required field")
    private Integer phoneNumber;

    public PhoneRequest(Integer ddd, Integer phoneNumber) {
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
    }

    public PhoneRequest(Phone phone) {
        ddd = phone.getDdd();
        phoneNumber = phone.getPhoneNumber();
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}