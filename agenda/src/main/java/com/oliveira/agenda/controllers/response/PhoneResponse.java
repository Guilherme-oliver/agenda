package com.oliveira.agenda.controllers.response;

import com.oliveira.agenda.entities.Phone;

public class PhoneResponse {

    private Integer ddd;
    private Integer phoneNumber;
    private ContactResponse contactResponse;

    public PhoneResponse(Integer ddd, Integer phoneNumber, ContactResponse contactResponse) {
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
        this.contactResponse = contactResponse;
    }

    public PhoneResponse(Phone phone) {
        ddd = phone.getDdd();
        phoneNumber = phone.getPhoneNumber();
        contactResponse = new ContactResponse(phone.getContact());
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

    public ContactResponse getContactResponse() {
        return contactResponse;
    }

    public void setContactResponse(ContactResponse contactResponse) {
        this.contactResponse = contactResponse;
    }
}