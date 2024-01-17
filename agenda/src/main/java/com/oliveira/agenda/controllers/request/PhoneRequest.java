package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Phone;

public class PhoneRequest {

    private Long id;
    private Integer ddd;
    private Integer phoneNumber;
    private ContactRequest contactRequest;

    public PhoneRequest(Long id,Integer ddd, Integer phoneNumber, ContactRequest contactRequest) {
        this.id = id;
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
        this.contactRequest = contactRequest;
    }

    public PhoneRequest(Phone phone) {
        id = phone.getId();
        ddd = phone.getDdd();
        phoneNumber = phone.getPhoneNumber();
        contactRequest = new ContactRequest(phone.getContact());
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

    public ContactRequest getContactRequest() {
        return contactRequest;
    }

    public void setContactRequest(ContactRequest contactRequest) {
        this.contactRequest = contactRequest;
    }
}