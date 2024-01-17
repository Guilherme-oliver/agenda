package com.oliveira.agenda.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_phone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ddd;
    private Integer phoneNumber;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public Phone() {
    }

    public Phone(Long id, Integer ddd, Integer phoneNumber, Contact contact) {
        this.id = id;
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
        this.contact = contact;
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

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}