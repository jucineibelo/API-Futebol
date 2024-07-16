package com.meli.futebol.services;

import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Estadio;
import com.meli.futebol.repositories.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio insertEstadio(EstadioDto estadioDto) {
        boolean existeEstadio = estadioRepository.existsByNome(estadioDto.getNome());
        if (existeEstadio) {
            throw new DuplicateKeyException("Error inserting estadio, because it already exists");
        }
        invalidSize(estadioDto.getNome());
        Estadio estadio = converter(estadioDto);
        estadioRepository.save(estadio);
        return estadio;
    }

    public Estadio updateEstadio(Long id, EstadioDto estadioDto) {
        Estadio estadio = estadioRepository.findById(id).orElseThrow(() -> new NotFoundException("Estadio not found"));
        boolean existeEstadio = estadioRepository.existsByNome(estadioDto.getNome());
        if (existeEstadio) {
            throw new DuplicateKeyException("Error updated estadio, because it already exists");
        }
        invalidSize(estadioDto.getNome());
        estadioRepository.save(estadio);
        return estadio;
    }

    public Estadio buscarEstadioById(Long id) {
        Estadio estadio = estadioRepository.findById(id).orElseThrow(() -> new NotFoundException("Estadio not found"));
        return estadio;
    }

    public Page<Estadio> listarEstadios(EstadioDto estadioDto, Pageable pageable) {
        return estadioRepository.findByNome(estadioDto.getNome(), pageable);
    }

    public void invalidSize(String nome) {
        if (nome.length() < 3 || nome.isEmpty() || nome == null) {
            throw new InvalidInputException("Invalid size name or empty");
        }
    }

    private Estadio converter(EstadioDto estadioDto) {
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDto.getNome());
        return estadio;
    }
}
