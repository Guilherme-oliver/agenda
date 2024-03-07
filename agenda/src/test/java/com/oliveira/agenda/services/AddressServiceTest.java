package com.oliveira.agenda.services;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.entities.Address;
import com.oliveira.agenda.entities.Contact;
import com.oliveira.agenda.repositories.AddressRepository;
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
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Address> page;
    private Address address;
    private AddressRequest addressRequest;
    private AddressResponse addressResponse;
    private Contact contact;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 5L;
        address = Factory.createAddress();
        addressRequest = Factory.createAddressRequest();
        addressResponse = Factory.createAddressResponse();
        page = new PageImpl<>(List.of(address));
        contact = Factory.createContact();


        when(addressRepository.findAll((Pageable) any())).thenReturn(page);

        when(addressRepository.save(any())).thenReturn(address);

        when(addressRepository.findById(existingId)).thenReturn(Optional.of(address));
        when(addressRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(addressRepository.getReferenceById(existingId)).thenReturn(address);
        when(addressRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(addressRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(addressRepository.existsById(nonExistingId)).thenReturn(false);
        when(addressRepository.existsById(existingId)).thenReturn(true);

        when(addressRepository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(addressRepository).deleteById(dependentId);

    }

    @Test
    public void updateShouldReturnOneAddressResponseWhenIdExists() {
        AddressResponse result = addressService.update(existingId, addressRequest);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.update(nonExistingId, addressRequest);
        });
    }

    @Test
    void testInsert() {
        when(addressRepository.existingAddress(anyString(), anyInt())).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(new Address());

        assertDoesNotThrow(() -> addressService.insert(contact, addressRequest));
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AddressResponse> result = addressService.findAll(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(addressRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnAddressResponseWhenIdExists() {
        AddressResponse result = addressService.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.findById(nonExistingId);
        });

        verify(addressRepository).findById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Long id = 1L;
        when(addressRepository.existsById(id)).thenReturn(true);

        // Act
        addressService.delete(id);

        // Assert
        verify(addressRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenReferentialIntegrityFailure() {
        Assertions.assertThrows(DatabaseException.class, ()-> {
            addressService.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.delete(nonExistingId);
        });
    }
}