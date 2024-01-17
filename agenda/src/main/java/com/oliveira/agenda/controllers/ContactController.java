package com.oliveira.agenda.controllers;

import com.oliveira.agenda.controllers.request.ContactRequest;
import com.oliveira.agenda.controllers.response.ContactResponse;
import com.oliveira.agenda.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContactResponse> findById(@PathVariable Long id) {
        ContactResponse dto = contactService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ContactResponse>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            Pageable pageable) {
        Page<ContactResponse> dto = contactService.findAll(name, pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContactResponse> insert(@Valid @RequestBody ContactRequest response) {
        var dto = contactService.insert(response);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id, @Valid @RequestBody ContactRequest response) {
        var dto = contactService.update(id, response);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
