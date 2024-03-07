package com.oliveira.agenda.controllers;

import com.oliveira.agenda.controllers.request.AddressRequest;
import com.oliveira.agenda.controllers.response.AddressResponse;
import com.oliveira.agenda.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id) {
        AddressResponse dto = addressService.findById(id);
        return ResponseEntity.ok(dto);
    }

    //Exibir a lista de endereços com paginação. Incluindo a opção para
    //navegar para a página seguinte ou página anterior.
    @GetMapping
    public ResponseEntity<Page<AddressResponse>> findAll(Pageable pageable) {
        Page<AddressResponse> dto = addressService.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    //Atualizar os dados de um endereço.
    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        var dto = addressService.update(id, request);
        return ResponseEntity.ok(dto);
    }
}