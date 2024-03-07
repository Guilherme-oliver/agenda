package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.request.ContactRequest;
import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.controllers.response.ContactResponse;
import com.oliveira.agenda.controllers.response.PhoneResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;
import com.oliveira.agenda.repositories.ContactRepository;
import com.oliveira.agenda.services.exceptions.DatabaseException;
import com.oliveira.agenda.services.exceptions.ResourceNotFoundException;
import com.oliveira.agenda.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ContactServiceTest{

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private PhoneService phoneService;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private String existingFirstName;
    private String existingLastName;
    private String nonExistingName;
    private PageImpl<Contact> page;
    private Contact contact;
    private ContactRequest contactRequest;
    private ContactResponse contactResponse;
    private Phone phone;
    private PhoneRequest phoneRequest;
    private PhoneResponse phoneResponse;
    private Address address;
    private AddressRequest addressRequest;
    private AddressResponse addressResponse;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 5L;
        existingFirstName = "FirstName";
        existingLastName = "LastName";
        nonExistingName ="null";
        contact = Factory.createContact();
        contactRequest = Factory.createContactRequest();
        contactResponse = Factory.createContactResponse();
        phone = Factory.createPhone();
        phoneRequest = Factory.createPhoneRequest();
        phoneResponse = Factory.createPhoneResponse();
        address = Factory.createAddress();
        addressRequest = Factory.createAddressRequest();
        addressResponse = Factory.createAddressResponse();
        page = new PageImpl<>(List.of(contact));


        when(contactRepository.findAll((Pageable) any())).thenReturn(page);

        when(contactRepository.save(any())).thenReturn(contact);

        when(contactRepository.findById(existingId)).thenReturn(Optional.of(contact));
        when(contactRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(contactRepository.searchByFirstName(existingFirstName)).thenReturn(Optional.of(contact));
        when(contactRepository.searchByLastName(existingLastName)).thenReturn(Optional.of(contact));
        when(contactRepository.searchByFirstName(nonExistingName)).thenThrow(ResourceNotFoundException.class);
        when(contactRepository.searchByLastName(nonExistingName)).thenThrow(ResourceNotFoundException.class);

        when(contactRepository.getReferenceById(existingId)).thenReturn(contact);
        when(contactRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(contactRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(contactRepository.existsById(nonExistingId)).thenReturn(false);
        when(contactRepository.existsById(existingId)).thenReturn(true);

        when(contactRepository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(contactRepository).deleteById(dependentId);

    }

    @Test
    public void updateShouldReturnOneUpdatedContactResponseWhenIdExists() {
        ContactResponse result = contactService.update(existingId, contactRequest);
        assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.update(nonExistingId, contactRequest);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContactResponse> result = contactService.findAll(pageable);
        assertNotNull(result);
        Mockito.verify(contactRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByFirstNameShouldReturnContactResponseWhenFirstNameExists() {
        ContactResponse response = contactService.findByFirstName(existingFirstName);
        assertNotNull(response);
    }

    @Test
    public void findByFirstNameShouldThrowResourceNotFoundWhenFirstNameNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.findByFirstName(nonExistingName);
        });
        verify(contactRepository).searchByFirstName(nonExistingName);
    }

    @Test
    public void findByLastNameShouldReturnContactResponseWhenLastNameExists() {
        ContactResponse response = contactService.findByLastName(existingLastName);
        assertNotNull(response);
    }

    @Test
    public void findByLastNameShouldThrowResourceNotFoundWhenLastNameNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.findByLastName(nonExistingName);
        });
        verify(contactRepository).searchByLastName(nonExistingName);
    }

    @Test
    public void findByIdShouldReturnContactResponseWhenIdExists() {
        ContactResponse result = contactService.findById(existingId);
        assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.findById(nonExistingId);
        });

        verify(contactRepository).findById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Long id = 1L;
        when(contactRepository.existsById(id)).thenReturn(true);
        contactService.delete(id);
        verify(contactRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenReferentialIntegrityFailure() {
        Assertions.assertThrows(DatabaseException.class, ()-> {
            contactService.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.delete(nonExistingId);
        });
    }

    @Test
    void testAddNewAddress() {
        when(contactRepository.findById(existingId)).thenReturn(java.util.Optional.of(contact));
        when(addressService.insert(contact, addressRequest)).thenReturn(addressResponse);

        ContactResponse result = contactService.addNewAddress(existingId, addressRequest);

        assertNotNull(result);
        assertEquals(result.getAddresses().get(0).getZipCode(), addressRequest.getZipCode());
    }

    @Test
    void testAddNewPhone() {
        when(contactRepository.findById(existingId)).thenReturn(java.util.Optional.of(contact));
        when(phoneService.insert(contact, phoneRequest)).thenReturn(phoneResponse);

        ContactResponse result = contactService.addNewPhone(existingId, phoneRequest);

        assertNotNull(result);
        assertEquals(result.getPhones().get(0).getPhoneNumber(), phoneRequest.getPhoneNumber());
    }

    @Test
    void testInsert() {
        ContactRequest contactRequest = new ContactRequest();
        when(contactRepository.existingContact(anyString(), anyString())).thenReturn(Optional.empty());
        when(contactRepository.save(any(Contact.class))).thenReturn(new Contact());

        assertDoesNotThrow(() -> contactService.insert(contactRequest));
    }
}