package com.oliveira.agenda.controllers.response;

import com.oliveira.agenda.entities.Contact;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactResponse {

    private String firstName;
    private String lastName;

    private List<AddressResponse> addresses = new ArrayList<>();
    private List<PhoneResponse> phones = new ArrayList<>();

    public ContactResponse() {
    }

    public ContactResponse(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ContactResponse(Contact entity) {
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
    }

    public Contact toContact() {
        Contact contact = new Contact();
        BeanUtils.copyProperties(this, contact);
        return contact;
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

    public List<AddressResponse> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressResponse> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneResponse> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneResponse> phones) {
        this.phones = phones;
    }
}