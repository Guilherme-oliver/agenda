package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.repositories.AddressRepository;
import com.oliveira.agenda.services.exceptions.DatabaseException;
import com.oliveira.agenda.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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

    @Autowired
    private ContactService contactService;

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id) {
        Address product = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not Found!"));
        return new AddressResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<AddressResponse> findAll(String publicPlace, Pageable pageable) {
        Page<Address> result = addressRepository.searchByStreet(publicPlace, pageable);
        return result.map(x -> new AddressResponse(x));
    }

    @Transactional
    public AddressResponse insert(AddressRequest dto) {
        Optional<Address> address = addressRepository.existingAddress(dto.getStreet(), dto.getAddressNumber());
        if (address.isPresent()) {
            new Exception("Address already exist");
        }
        try {
            Contact contact = contactService.findById(dto.getContactRequest().getId()).toContact();
            Address entity = new Address();
            copyDtoToEntity(dto, entity, contact);
            entity = addressRepository.save(entity);
            return new AddressResponse(entity);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AddressResponse update(Long id, @Valid AddressRequest dto) {
        Optional<Address> address = addressRepository.existingAddress(dto.getStreet(), dto.getAddressNumber());

        if (address.isPresent()) {
            new Exception("Contact already exist");
        }

        try {
            Contact contact = contactService.findById(dto.getContactRequest().getId()).toContact();
            Address entity = addressRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity, contact);
            entity = addressRepository.save(entity);
            return new AddressResponse(entity);
        }catch (EntityNotFoundException e) {
            throw new  ResourceNotFoundException("Resource not found!");
        }
    }

    private void copyDtoToEntity(AddressRequest dto, Address entity, Contact contact) {
        entity.setZipCode(dto.getZipCode());
        entity.setAddressNumber(dto.getAddressNumber());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setContact(contact);
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