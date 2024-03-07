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

    //Exibir todas as informações de um contato da agenda.
    @Transactional(readOnly = true)
    public ContactResponse findById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    // Buscar um contato de acordo com uma palavra-chave (Utilize os dados
    //de nome, sobrenome para realizar a busca).
    @Transactional(readOnly = true)
    public ContactResponse findByFirstName(String firstName) {
        Contact contact = contactRepository.searchByFirstName(firstName).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    //• Buscar um contato de acordo com uma palavra-chave (Utilize os dados
    //de nome, sobrenome para realizar a busca).
    @Transactional(readOnly = true)
    public ContactResponse findByLastName(String lastName) {
        Contact contact = contactRepository.searchByLastName(lastName).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new ContactResponse(contact);
    }

    //Listar todos os contatos da agenda.
    @Transactional(readOnly = true)
    public Page<ContactResponse> findAll(Pageable pageable) {
        Page<Contact> result = contactRepository.findAll(pageable);
        return result.map(x -> new ContactResponse(x));
    }

    private void copyDtoToEntity(ContactRequest response, Contact entity) {
        entity.setFirstName(response.getFirstName());
        entity.setLastName(response.getLastName());
    }

    //Adicionar um endereço a um contato.
    @Transactional(readOnly = true)
    public ContactResponse addNewAddress(Long id, AddressRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        if (contact.getAddresses().stream().anyMatch(t -> t.getZipCode().equals(request.getZipCode()))) {
            throw new DatabaseException("The address number already exists for this contact");
        }
        Address address = new Address();
        address.setId(request.getId());
        address.setZipCode(request.getZipCode());
        address.setAddressNumber(request.getAddressNumber());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setContact(contact);
        contact.getAddresses().add(address);
        addressService.insert(contact, request);
        return new ContactResponse(contact);
    }

    //Adicionar um telefone a um contato.
    @Transactional(readOnly = true)
    public ContactResponse addNewPhone(Long id, PhoneRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        if (contact.getPhones().stream().anyMatch(t -> t.getPhoneNumber().equals(request.getPhoneNumber()))) {
            throw new DatabaseException("The phone number already exists for this contact");
        }
        Phone phone = new Phone();
        phone.setId(request.getId());
        phone.setDdd(request.getDdd());
        phone.setPhoneNumber(request.getPhoneNumber());
        phone.setContact(contact);
        contact.getPhones().add(phone);
        phoneService.insert(contact, request);
        return new ContactResponse(contact);
    }

    //Adicionar um contato e seus dados.
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

    //Atualizar os dados de um contato.
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

    //Remover um contato da agenda.
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

    //Remover todos os contatos da agenda.
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAll() {
        contactRepository.deleteAll();
    }

    //Remover um telefone de um contato da agenda.
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletePhone(Long id, PhoneRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        phoneService.delete(request.getId());
    }

    //Remover um endereço de um contato da agenda.
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAddress(Long id, AddressRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        addressService.delete(request.getId());
    }
}