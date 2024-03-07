package com.oliveira.agenda.controllers.request;

import com.oliveira.agenda.entities.Address;
import jakarta.validation.constraints.*;

public class AddressRequest {

    private Long id;

    @NotNull(message = "Required field")
    @Positive(message = "The zip code must be positive")
    private Long zipCode;

    @Size(min = 3)
    @NotBlank(message = "Description must have at least 3 characters")
    private String street;

    @NotNull(message = "Required field")
    @Positive(message = "The address number must be positive")
    private Integer addressNumber;

    @Size(min = 3)
    @NotBlank(message = "City must have at least 3 characters")
    private String city;

    @Size(min = 2)
    @NotBlank(message = "State must have at least 2 characters")
    private String state;

    public AddressRequest(Long id, Long zipCode, String street, Integer addressNumber, String city, String state) {
        this.id = id;
        this.zipCode = zipCode;
        this.street = street;
        this.addressNumber = addressNumber;
        this.city = city;
        this.state = state;
    }

    public AddressRequest(Address entity) {
        id = entity.getId();
        zipCode = entity.getZipCode();
        street = entity.getStreet();
        addressNumber = entity.getAddressNumber();
        city = entity.getCity();
        state = entity.getState();
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
}