package com.meli.futebol.controllers;

import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Estadio;
import com.meli.futebol.services.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody EstadioDto estadioDto) {   //metodo Post inserir estadio - retorno esperado: 201 Created
        try {
            Estadio estadio = estadioService.insertEstadio(estadioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(estadio);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody EstadioDto estadioDto) {    //metodo Put atualizar estadio - retorno esperado: 200 Ok
        try {
            Estadio estadio = estadioService.updateEstadio(id, estadioDto);
            return ResponseEntity.ok(estadio);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {                 //metodo Get buscar estadio por Id - retorno esperado: 200 ok
        try {
            return ResponseEntity.ok(estadioService.getEstadioById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Estadio>> getListarEstadios(@RequestBody EstadioDto estadioDto, Pageable pageable) {  //Metodo Get listar estadios - retorno esperado: 200 ok
        return ResponseEntity.ok(estadioService.getEstadioList(estadioDto, pageable));
    }

}
