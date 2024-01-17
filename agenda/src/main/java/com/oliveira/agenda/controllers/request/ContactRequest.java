package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRequest {

    private Long id;
    private String firstName;
    private String lastName;

    private List<AddressRequest> addresses = new ArrayList<>();
    private List<PhoneRequest> phones = new ArrayList<>();

    public ContactRequest() {
    }

    public ContactRequest(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ContactRequest(Contact entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<AddressRequest> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRequest> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneRequest> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequest> phones) {
        this.phones = phones;
    }
}