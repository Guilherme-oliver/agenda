package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Phone;
import jakarta.validation.constraints.*;

public class PhoneRequest {

    private Long id;

    @Digits(integer = 2, fraction = 0, message = "Phone number must be a valid number with up to 2 digits.")
    @NotNull(message = "Required field")
    private Integer ddd;

    @Digits(integer = 9, fraction = 0, message = "Phone number must be a valid number with up to 10 digits.")
    @NotNull(message = "Required field")
    private Integer phoneNumber;

    public PhoneRequest(Long id, Integer ddd, Integer phoneNumber) {
        this.id = id;
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
    }

    public PhoneRequest(Phone phone) {
        id = phone.getId();
        ddd = phone.getDdd();
        phoneNumber = phone.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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