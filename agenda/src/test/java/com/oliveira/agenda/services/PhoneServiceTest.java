package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.PhoneResponse;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.entities.Phone;
import com.oliveira.agenda.repositories.PhoneRepository;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PhoneServiceTest {

    @InjectMocks
    private PhoneService phoneService;

    @Mock
    private PhoneRepository phoneRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Phone> page;
    private Phone phone;
    private PhoneRequest phoneRequest;
    private PhoneResponse phoneResponse;
    private Contact contact;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 5L;
        phone = Factory.createPhone();
        phoneRequest = Factory.createPhoneRequest();
        phoneResponse = Factory.createPhoneResponse();
        contact = Factory.createContact();
        page = new PageImpl<>(List.of(phone));

        when(phoneRepository.findAll((Pageable) any())).thenReturn(page);

        when(phoneRepository.save(any())).thenReturn(phone);

        when(phoneRepository.findById(existingId)).thenReturn(Optional.of(phone));
        when(phoneRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(phoneRepository.getReferenceById(existingId)).thenReturn(phone);
        when(phoneRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(phoneRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(phoneRepository.existsById(nonExistingId)).thenReturn(false);
        when(phoneRepository.existsById(existingId)).thenReturn(true);

        when(phoneRepository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(phoneRepository).deleteById(dependentId);

    }

    @Test
    public void updateShouldReturnUpdatedPhoneResponseWhenIdExists() {
        PhoneResponse result = phoneService.update(existingId, phoneRequest);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            phoneService.update(nonExistingId, phoneRequest);
        });
    }

    @Test
    void testInsert() {
        PhoneRequest phoneRequest = Factory.createPhoneRequest();
        when(phoneRepository.existingContact(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(phoneRepository.save(any(Phone.class))).thenReturn(new Phone());

        assertDoesNotThrow(() -> phoneService.insert(contact, phoneRequest));
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PhoneResponse> result = phoneService.findAll(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(phoneRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnPhoneResponseWhenIdExists() {
        PhoneResponse result = phoneService.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            phoneService.findById(nonExistingId);
        });

        verify(phoneRepository).findById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Long id = 1L;
        when(phoneRepository.existsById(id)).thenReturn(true);

        // Act
        phoneService.delete(id);

        // Assert
        verify(phoneRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenReferentialIntegrityFailure() {
        Assertions.assertThrows(DatabaseException.class, ()-> {
            phoneService.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            phoneService.delete(nonExistingId);
        });
    }
}