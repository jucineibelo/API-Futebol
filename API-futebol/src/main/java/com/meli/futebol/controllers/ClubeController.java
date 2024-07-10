package com.meli.futebol.controllers;

import com.meli.futebol.dtos.ClubeDto;
import com.meli.futebol.dtos.FiltroClubeDto;
import com.meli.futebol.exceptons.NotFoundException;
import com.meli.futebol.models.Clube;
import com.meli.futebol.services.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping("/clubes")
    public ResponseEntity<?> post(@RequestBody ClubeDto clubeDto) {
        try {
            Clube clube = clubeService.insertClube(clubeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clube); //retorno post created 201
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/clubes/{id}")
    public ResponseEntity<Clube> put(@PathVariable Long id, @RequestBody ClubeDto clubeDto) {
        Clube clube = clubeService.updateClube(id, clubeDto);
        if (clube != null) {
            return ResponseEntity.ok(clube);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clubes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (clubeService.inactivateClube(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/clubes/{id}")
    public ResponseEntity<Clube> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clubeService.listarClubeById(id));
    }

    @GetMapping("/clubes")
    public ResponseEntity<Page<Clube>> getListarClubes(@RequestBody FiltroClubeDto filtroClubeDto, Pageable pageable) {
        return ResponseEntity.ok(clubeService.listarClubes(filtroClubeDto, pageable));
    }


}






