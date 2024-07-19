package com.meli.futebol.controllers;

import com.meli.futebol.dtos.FiltroPartidaDto;
import com.meli.futebol.dtos.PartidaDto;
import com.meli.futebol.exceptions.ConflitException;
import com.meli.futebol.exceptions.DateInvalidException;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Partida;
import com.meli.futebol.services.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody PartidaDto partidaDto) {
        try {
            Partida partida = partidaService.insertPartida(partidaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(partida);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DateInvalidException | ConflitException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados incompletos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody PartidaDto partidaDto) {
        try {
            Partida partida = partidaService.updatePartida(id, partidaDto);
            return ResponseEntity.status(HttpStatus.OK).body(partida);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DateInvalidException | ConflitException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados incompletos");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            partidaService.deletePartida(id);
            return ResponseEntity.status(HttpStatus.OK).body("Partida " + id + " deleted with sucess");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida not found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Partida partida = partidaService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(partida);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida not found");
        }
    }

    @GetMapping
    public ResponseEntity<Page<Partida>> getPartidas(@RequestBody FiltroPartidaDto filtroPartidaDto, Pageable pageable) {
        return ResponseEntity.ok(partidaService.listarPartidas(filtroPartidaDto, pageable));
    }

}
