package com.oliveira.agenda.controllers;

import com.oliveira.agenda.controllers.request.PhoneRequest;
import com.oliveira.agenda.controllers.response.PhoneResponse;
import com.oliveira.agenda.services.PhoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/phone")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PhoneResponse> findById(@PathVariable Long id) {
        PhoneResponse dto = phoneService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<PhoneResponse>> findAll(
            @RequestParam Integer phoneNumber,
            Pageable pageable) {
        Page<PhoneResponse> dto = phoneService.findAll(phoneNumber, pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PhoneResponse> insert(@Valid @RequestBody PhoneRequest request) {
        var dto = phoneService.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PhoneResponse> update(@PathVariable Long id, @Valid @RequestBody PhoneRequest request) {
        var dto = phoneService.update(id, request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        phoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}