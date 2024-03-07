package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.repositories.AddressRepository;
import com.oliveira.agenda.services.exceptions.DatabaseException;
import com.oliveira.agenda.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id) {
        Address product = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not Found!"));
        return new AddressResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<AddressResponse> findAll(Pageable pageable) {
        Page<Address> result = addressRepository.findAll(pageable);
        return result.map(x -> new AddressResponse(x));
    }

    @Transactional
    public AddressResponse insert(Contact contact, AddressRequest dto) {
        Optional<Address> address = addressRepository.existingAddress(dto.getStreet(), dto.getAddressNumber());
        if (address.isPresent()) {
            throw new RuntimeException("Address already exist!");
        } else {
            Address entity = new Address();
            copyDtoToEntity(dto, entity);
            entity.setContact(contact);
            entity = addressRepository.save(entity);
            return new AddressResponse(entity);
        }
    }

    //Atualizar os dados de um endereço.
    @Transactional
    public AddressResponse update(Long id, AddressRequest dto) {
        try {
            Address entity = addressRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = addressRepository.save(entity);
            return new AddressResponse(entity);
        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    private void copyDtoToEntity(AddressRequest dto, Address entity) {
        entity.setZipCode(dto.getZipCode());
        entity.setAddressNumber(dto.getAddressNumber());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found!");
        }
        try {
            addressRepository.deleteById(id);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure!");
        }
    }
}