package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.request.ContactRequest;
import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.ContactResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;
import com.oliveira.agenda.repositories.ContactRepository;
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
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PhoneService phoneService;

    @Transactional(readOnly = true)
    public ContactResponse findById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse findByFistName(String firstName) {
        Contact contact = contactRepository.searchByFirstName(firstName).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse findByLastName(String lastName) {
        Contact contact = contactRepository.searchByLastName(lastName).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> findAll(String name, Pageable pageable) {
        Page<Contact> result = contactRepository.searchByName(name, pageable);
        return result.map(x -> new ContactResponse(x));
    }

    private void copyDtoToEntity(ContactRequest response, Contact entity) {
        entity.setFirstName(response.getFirstName());
        entity.setLastName(response.getLastName());
    }

    @Transactional(readOnly = true)
    public ContactResponse addNewAddress(Long id, AddressRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        addressService.insert(contact, request);
        return new ContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse addNewPhone(Long id, PhoneRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        phoneService.insert(contact, request);
        return new ContactResponse(contact);
    }

    @Transactional
    public ContactResponse insert(ContactRequest contactRequest) {
        Optional<Contact> contact = contactRepository.existingContact(contactRequest.getFirstName(), contactRequest.getLastName());
        if (contact.isPresent()) {
            throw new RuntimeException("Contact already exist!");
        } else {
            try {
                Contact entity = new Contact();
                entity.setId(null);
                copyDtoToEntity(contactRequest, entity);

                for (Address address : entity.getAddresses()) {
                    address.setContact(entity);
                }

                for (Phone phone : entity.getPhones()) {
                    phone.setContact(entity);
                }

                entity = contactRepository.save(entity);
                return new ContactResponse(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    public ContactResponse update(Long id, ContactRequest response) {
        try {
            Contact entity = contactRepository.getReferenceById(id);
            copyDtoToEntity(response, entity);
            entity = contactRepository.save(entity);
            return new ContactResponse(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found!");
        }
        try {
            contactRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAll() {
        contactRepository.deleteAll();
    }
}