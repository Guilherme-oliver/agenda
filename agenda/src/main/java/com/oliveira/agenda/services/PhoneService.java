package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.PhoneResponse;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;
import com.oliveira.agenda.repositories.PhoneRepository;
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
public class PhoneService {

    private PhoneRepository phoneRepository;

    @Autowired
    private ContactService contactService;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Transactional(readOnly = true)
    public PhoneResponse findById(Long id) {
        Phone phone = phoneRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found!"));
        return new PhoneResponse(phone);
    }

    @Transactional(readOnly = true)
    public Page<PhoneResponse> findAll(Integer phoneNumber, Pageable pageable) {
        Page<Phone> phones = phoneRepository.searchByPhoneNumber(phoneNumber, pageable);
        return phones.map(x -> new PhoneResponse(x));
    }

    private void copyDtoToEntity(PhoneRequest request, Phone entity) {
        entity.setDdd(request.getDdd());
        entity.setPhoneNumber(request.getPhoneNumber());
    }

    @Transactional
    public PhoneResponse insert(PhoneRequest request) {
        Optional<Phone> phone = phoneRepository.existingContact(request.getDdd(), request.getPhoneNumber());
        if (phone.isPresent()) {
            throw new RuntimeException("Phone already exist!");
        }
        try {
            Contact contact = contactService.findById(request.getContactRequest().getId()).toContact();
            Phone entity = new Phone();
            copyDtoToEntity(request, entity);
            entity = phoneRepository.save(entity);
            return new PhoneResponse(entity);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PhoneResponse update(Long id, PhoneRequest response) {
        try {
            Phone entity = phoneRepository.getReferenceById(id);
            copyDtoToEntity(response, entity);
            entity = phoneRepository.save(entity);
            return new PhoneResponse(entity);
        } catch (EntityNotFoundException e) {
            throw new  ResourceNotFoundException("Resource not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!phoneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found!");
        }
        try {
            phoneRepository.deleteById(id);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure!");
        }
    }
}