package com.meli.futebol.controllers;

import com.meli.futebol.dtos.FiltroPartidaDto;
import com.meli.futebol.dtos.PartidaDto;
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

    @PostMapping("/partidas")
    public ResponseEntity<Partida> post(@RequestBody PartidaDto partidaDto) {
        Partida partida = partidaService.insertPartida(partidaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(partida);
    }

    @PutMapping("/partidas/{id}")
    public ResponseEntity<Partida> put(@PathVariable Long id, @RequestBody PartidaDto partidaDto) {
        Partida partida = partidaService.updatePartida(id, partidaDto);
        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @DeleteMapping("/partidas/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        partidaService.deletePartida(id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @GetMapping("/partidas/{id}")
    public ResponseEntity<Partida> getById(@PathVariable Long id) {
        Partida partida = partidaService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @GetMapping("/partidas")
    public ResponseEntity<Page<Partida>> getPartidas(@RequestBody FiltroPartidaDto filtroPartidaDto, Pageable pageable){
        return ResponseEntity.ok(partidaService.listarPartidas(filtroPartidaDto, pageable));
    }

}
