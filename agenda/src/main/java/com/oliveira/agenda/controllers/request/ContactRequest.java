package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ContactRequest {

    private Long id;

    @Size(min = 3, max = 80, message = "min 3 and max 80")
    @NotBlank(message = "Required field")
    private String firstName;

    @Size(min = 3, max = 80, message = "min 3 and max 80")
    @NotBlank(message = "Required field")
    private String lastName;

    @NotEmpty(message = "Must have at least one address")
    private List<AddressRequest> addresses = new ArrayList<>();

    @NotEmpty(message = "Must have at least one phone")
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
        for (Address address : entity.getAddresses()) {
            addresses.add(new AddressRequest(address));
        }
        for (Phone phone : entity.getPhones()) {
            phones.add(new PhoneRequest(phone));
        }
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