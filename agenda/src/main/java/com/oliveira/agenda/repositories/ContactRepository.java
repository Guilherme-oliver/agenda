package com.oliveira.agenda.repositories;

import com.oliveira.agenda.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT contact FROM Contact contact WHERE contact.firstName = :firstName AND contact.lastName = :lastName")
    Optional<Contact> existingContact(String firstName, String lastName);

    @Query("SELECT ob FROM Contact ob WHERE UPPER(ob.firstName) LIKE UPPER(CONCAT('%', :firstName, '%'))")
    Optional<Contact> searchByFirstName(String firstName);

    @Query("SELECT bj FROM Contact bj WHERE UPPER(bj.lastName) LIKE UPPER(CONCAT('%', :lastName, '%'))")
    Optional<Contact> searchByLastName(String lastName);
}