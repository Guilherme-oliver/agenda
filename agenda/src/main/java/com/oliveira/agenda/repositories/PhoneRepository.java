package com.oliveira.agenda.repositories;

import com.oliveira.agenda.entities.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query("SELECT ph FROM Phone ph WHERE ph.phoneNumber = :phoneNumber")
    Page<Phone> searchByPhoneNumber(Integer phoneNumber, Pageable pageable);

    @Query("SELECT phone FROM Phone phone WHERE phone.ddd = :ddd AND phone.phoneNumber = :phoneNumber")
    Optional<Phone> existingContact(Integer ddd, Integer phoneNumber);
}