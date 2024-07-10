package com.meli.futebol.services;

import com.meli.futebol.dtos.ClubeDto;
import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.dtos.FiltroPartidaDto;
import com.meli.futebol.dtos.PartidaDto;
import com.meli.futebol.exceptons.NotFoundException;
import com.meli.futebol.models.Partida;
import com.meli.futebol.repositories.ClubeRepository;
import com.meli.futebol.repositories.EstadioRepository;
import com.meli.futebol.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    @Autowired
    PartidaRepository partidaRepository;

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    EstadioRepository estadioRepository;

    public Partida insertPartida(PartidaDto partidaDto) {
        Partida partida = convertToEntity(partidaDto);
        partidaRepository.save(partida);
        return partida;
    }

    public Partida updatePartida(Long id, PartidaDto partidaDto) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new NotFoundException("Partida not found"));
        partida.setResultado(partidaDto.getResultado());
        partida.setDataHora(partidaDto.getDataHora());
        partidaRepository.save(partida);
        return partida;
    }

    public void deletePartida(Long id) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new NotFoundException("Partida not found"));
        partidaRepository.delete(partida);
    }

    public Partida getById(Long id) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new NotFoundException("Partida not found"));
        return partida;
    }

    public Page<Partida> listarPartidas(FiltroPartidaDto filtroPartidaDto, Pageable pageable) {  //listarPartidas get
        return partidaRepository.findByFilters(filtroPartidaDto.getNomeClube(), filtroPartidaDto.getNomeEstadio(), pageable);
    }

    private Partida convertToEntity(PartidaDto partidaDto) {
        Partida partida = new Partida();
        partida.setClubeCasa(clubeRepository.findById(partidaDto.getIdClubeCasa()).orElseThrow(() -> new NotFoundException("Clube casa not found")));
        partida.setClubeVisitante(clubeRepository.findById(partidaDto.getIdclubeVisitante()).orElseThrow(() -> new NotFoundException("Clube visitante not found")));
        partida.setEstadio(estadioRepository.findById(partidaDto.getIdEstadio()).orElseThrow(() -> new NotFoundException("Estadio not found")));
        partida.setResultado(partidaDto.getResultado());
        partida.setDataHora(partidaDto.getDataHora());
        return partida;
    }

}
