package com.oliveira.agenda.controllers.response;

import com.oliveira.agenda.entities.Phone;

public class PhoneResponse {

    private Integer ddd;
    private Integer phoneNumber;

    public PhoneResponse(Integer ddd, Integer phoneNumber) {
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
    }

    public PhoneResponse(Phone phone) {
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