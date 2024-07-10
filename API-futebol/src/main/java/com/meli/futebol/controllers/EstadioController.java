package com.meli.futebol.controllers;

import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.models.Estadio;
import com.meli.futebol.services.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping("/estadios")
    public ResponseEntity<Estadio> post(@RequestBody EstadioDto estadioDto) {   //metodo Post inserir estadio - retorno esperado: 201 Created
        Estadio estadio = estadioService.insertEstadio(estadioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadio);
    }

    @PutMapping("/estadios/{id}")
    public ResponseEntity<Estadio> put(@PathVariable Long id, @RequestBody EstadioDto estadioDto) {    //metodo Put atualizar estadio - retorno esperado: 200 Ok
        Estadio estadio = estadioService.updateEstadio(id, estadioDto);
        if (estadio != null) {
            return ResponseEntity.ok(estadio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadios/{id}")
    public ResponseEntity<Estadio> getById(@PathVariable Long id) {                 //metodo Get buscar estadio por Id - retorno esperado: 200 ok
        return ResponseEntity.ok(estadioService.buscarEstadioById(id));
    }

    @GetMapping("/estadios")
    public ResponseEntity<Page<Estadio>> getListarEstadios(@RequestBody EstadioDto estadioDto, Pageable pageable) {  //Metodo Get listar estadios - retorno esperado: 200 ok
        return ResponseEntity.ok(estadioService.listarEstadios(estadioDto, pageable));
    }

}
