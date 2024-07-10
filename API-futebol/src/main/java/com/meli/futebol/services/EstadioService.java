package com.meli.futebol.services;

import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.exceptons.NotFoundException;
import com.meli.futebol.models.Estadio;
import com.meli.futebol.repositories.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio insertEstadio(EstadioDto estadioDto) {
        Estadio estadio = converter(estadioDto);
        estadioRepository.save(estadio);
        return estadio;
    }

    public Estadio updateEstadio(Long id, EstadioDto estadioDto) {
        Estadio estadio = estadioRepository.findById(id).orElseThrow(() -> new NotFoundException("Estadio not found"));
        if (estadioDto.getNome() != null) {
            estadio.setNome(estadioDto.getNome());
        }
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

    private Estadio converter(EstadioDto estadioDto) {
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDto.getNome());
        return estadio;
    }
}
