package com.oliveira.agenda.repositories;

import com.oliveira.agenda.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT obj FROM Contact obj WHERE UPPER(obj.firstName) LIKE UPPER(CONCAT('%', :firstName, '%'))")
    Page<Contact> searchByName(String firstName, Pageable pageable);

    @Query("SELECT contact FROM Contact contact WHERE contact.firstName = :firstName AND contact.lastName = :lastName")
    Optional<Contact> existingContact(String firstName, String lastName);

    Optional<Contact> searchByFirstName(String firstName);
    Optional<Contact> searchByLastName(String lastName);
}