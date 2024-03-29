package com.oliveira.agenda.controllers.response;

import com.oliveira.agenda.entities.Address;

public class AddressResponse {

    private Long zipCode;
    private String street;
    private Integer addressNumber;
    private String city;
    private String state;

    public AddressResponse(Long zipCode, String street, Integer addressNumber, String city, String state) {
        this.zipCode = zipCode;
        this.street = street;
        this.addressNumber = addressNumber;
        this.city = city;
        this.state = state;
    }

    public AddressResponse(Address entity) {
        zipCode = entity.getZipCode();
        street = entity.getStreet();
        addressNumber = entity.getAddressNumber();
        city = entity.getCity();
        state = entity.getState();
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
}