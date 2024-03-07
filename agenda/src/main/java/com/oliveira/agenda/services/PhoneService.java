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

    @Autowired
    private PhoneRepository phoneRepository;

    @Transactional(readOnly = true)
    public PhoneResponse findById(Long id) {
        Phone product = phoneRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not Found!"));
        return new PhoneResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<PhoneResponse> findAll(Pageable pageable) {
        Page<Phone> result = phoneRepository.findAll(pageable);
        return result.map(x -> new PhoneResponse(x));
    }

    @Transactional
    public PhoneResponse insert(Contact contact, PhoneRequest dto) {
        Optional<Phone> phone = phoneRepository.existingContact(dto.getDdd(), dto.getPhoneNumber());
        if (phone.isPresent()) {
            throw new RuntimeException("Phone already exist!");
        } else {
            Phone entity = new Phone();
            copyDtoToEntity(dto, entity);
            entity.setContact(contact);
            entity = phoneRepository.save(entity);
            return new PhoneResponse(entity);
        }
    }

    //Atualizar os dados de um telefone.
    @Transactional
    public PhoneResponse update(Long id, PhoneRequest dto) {
        try {
            Phone entity = phoneRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = phoneRepository.save(entity);
            return new PhoneResponse(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    private void copyDtoToEntity(PhoneRequest dto, Phone entity) {
        entity.setDdd(dto.getDdd());
        entity.setPhoneNumber(dto.getPhoneNumber());
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