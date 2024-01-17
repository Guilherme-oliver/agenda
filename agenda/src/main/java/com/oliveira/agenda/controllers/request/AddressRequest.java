package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Address;

public class AddressRequest {

    private Long id;
    private Long zipCode;
    private String street;
    private Integer addressNumber;
    private String city;
    private String state;
    private ContactRequest contactRequest;

    public AddressRequest(Long id, Long zipCode, String street, Integer addressNumber, String city, String state, ContactRequest contactRequest) {
        this.id = id;
        this.zipCode = zipCode;
        this.street = street;
        this.addressNumber = addressNumber;
        this.city = city;
        this.state = state;
        this.contactRequest = contactRequest;
    }

    public AddressRequest(Address entity) {
        id = entity.getId();
        zipCode = entity.getZipCode();
        street = entity.getStreet();
        addressNumber = entity.getAddressNumber();
        city = entity.getCity();
        state = entity.getState();
        contactRequest = new ContactRequest(entity.getContact());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ContactRequest getContactRequest() {
        return contactRequest;
    }

    public void setContactRequest(ContactRequest contactRequest) {
        this.contactRequest = contactRequest;
    }
}