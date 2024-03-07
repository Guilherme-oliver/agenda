package com.oliveira.agenda.repositories;

import com.oliveira.agenda.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT ob FROM Address ob WHERE UPPER(ob.street) LIKE UPPER(CONCAT('%', :street, '%'))")
    Optional<Address> searchByStreet(String street);

    @Query("SELECT add FROM Address add WHERE add.street = :street AND add.addressNumber = :addressNumber")
    Optional<Address> existingAddress(String street, Integer addressNumber);
}