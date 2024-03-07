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

@RestController
@RequestMapping(value = "/phone")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PhoneResponse> findById(@PathVariable Long id) {
        PhoneResponse dto = phoneService.findById(id);
        return ResponseEntity.ok(dto);
    }

    //Exibir a lista de telefones com paginação. Incluindo a opção para navegar
    //para a página seguinte ou página anterior.
    @GetMapping
    public ResponseEntity<Page<PhoneResponse>> findAll(Pageable pageable) {
        Page<PhoneResponse> dto = phoneService.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    //Atualizar os dados de um telefone.
    @PutMapping(value = "/{id}")
    public ResponseEntity<PhoneResponse> update(@PathVariable Long id, @Valid @RequestBody PhoneRequest request) {
        var dto = phoneService.update(id, request);
        return ResponseEntity.ok(dto);
    }
}