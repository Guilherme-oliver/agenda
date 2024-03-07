package com.oliveira.agenda.tests;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.request.ContactRequest;
import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.controllers.response.ContactResponse;
import com.oliveira.agenda.controllers.response.PhoneResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;

public class Factory {

    public static Contact createContact() {
        Contact contact = new Contact(1L, "João", "Pereira");
        return contact;
    }

    public static ContactRequest createContactRequest() {
        Contact contact = createContact();
        return new ContactRequest(contact);
    }

    public static ContactResponse createContactResponse() {
        Contact contact = createContact();
        return new ContactResponse(contact);
    }

    public static Address createAddress() {
        Address address = new Address(1L, 123456798L, "Belarmino", 123, "Uberlândia", "MG", createContact());
        return address;
    }

    public static AddressRequest createAddressRequest() {
        Address address = createAddress();
        return new AddressRequest(address);
    }

    public static AddressResponse createAddressResponse() {
        Address address = createAddress();
        return new AddressResponse(address);
    }

    public static Phone createPhone() {
        Phone phone = new Phone(1L, 34, 999999999, createContact());
        return phone;
    }

    public static PhoneRequest createPhoneRequest() {
        Phone phone = createPhone();
        return new PhoneRequest(phone);
    }

    public static PhoneResponse createPhoneResponse() {
        Phone phone = createPhone();
        return new PhoneResponse(phone);
    }
}