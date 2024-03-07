package com.oliveira.agenda.controllers;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.request.ContactRequest;
import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.ContactResponse;
import com.oliveira.agenda.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContactResponse> findById(@PathVariable Long id) {
        ContactResponse dto = contactService.findById(id);
        return ResponseEntity.ok(dto);
    }

    //Buscar um contato de acordo com uma palavra-chave (Utilize os dados
    //de nome, sobrenome para realizar a busca).
    @GetMapping(value = "/{firstName}/first-name")
    public ResponseEntity<ContactResponse> searchByFirstName(@PathVariable String firstName) {
        ContactResponse dto = contactService.findByFirstName(firstName);
        return ResponseEntity.ok(dto);
    }

    //Buscar um contato de acordo com uma palavra-chave (Utilize os dados
    //de nome, sobrenome para realizar a busca).
    @GetMapping(value = "/{lastName}/last-name")
    public ResponseEntity<ContactResponse> findById(@PathVariable String lastName) {
        ContactResponse dto = contactService.findByLastName(lastName);
        return ResponseEntity.ok(dto);
    }

    /*Listar todos os contatos da agenda.
    Exibir a lista de contatos com paginação. Incluindo a opção para navegar
    para a página seguinte ou página anterior.
     */
    @GetMapping
    public ResponseEntity<Page<ContactResponse>> findAll(Pageable pageable) {
        Page<ContactResponse> dto = contactService.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    //Adicionar um contato e seus dados.
    @PostMapping
    public ResponseEntity<ContactResponse> insert(@Valid @RequestBody ContactRequest response) {
        var dto = contactService.insert(response);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //Adicionar um telefone a um contato.
    @PostMapping(value = "/{contactId}/add-phone")
    public ResponseEntity<ContactResponse> addNewPhone(@Valid @RequestBody PhoneRequest request, @PathVariable Long contactId) {
        var dto = contactService.addNewPhone(contactId, request);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //Adicionar um endereço a um contato.
    @PostMapping(value = "/{contactId}/add-address")
    public ResponseEntity<ContactResponse> addNewAddress (@Valid @RequestBody AddressRequest request, @PathVariable Long contactId) {
        var dto = contactService.addNewAddress(contactId, request);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //Atualizar os dados de um contato.
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id, @Valid @RequestBody ContactRequest response) {
        var dto = contactService.update(id, response);
        return ResponseEntity.ok(dto);
    }

    //Remover um contato da agenda.
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Remover todos os contatos da agenda.
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        contactService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    //Remover um telefone de um contato da agenda.
    @DeleteMapping(value = "delete-phone/{id}")
    public ResponseEntity<Void> deletePhoneFromContact(@PathVariable Long id, @RequestBody PhoneRequest request) {
        contactService.deletePhone(id, request);
        return ResponseEntity.noContent().build();
    }

    //Remover um endereço de um contato da agenda.
    @DeleteMapping(value = "delete-address/{id}")
    public ResponseEntity<Void> deleteAddressFromContact(@PathVariable Long id, @RequestBody AddressRequest request) {
        contactService.deleteAddress(id, request);
        return ResponseEntity.noContent().build();
    }
}