package com.meli.futebol.services;

import com.meli.futebol.dtos.ClubeDto;
import com.meli.futebol.dtos.FiltroClubeDto;
import com.meli.futebol.exceptons.NotFoundException;
import com.meli.futebol.models.Clube;
import com.meli.futebol.repositories.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    public Clube insertClube(ClubeDto clubeDto) {
        Clube clube = convert(clubeDto);
        clubeRepository.save(clube);
        return clube;
    }

    public Clube updateClube(Long id, ClubeDto clubeDto) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new NotFoundException("Clube not found"));
        if (clubeDto.getNome() != null) {
            clube.setNome(clubeDto.getNome());
        }
        if (clubeDto.getSiglaEstado() != null) {
            clube.setSiglaEstado(clubeDto.getSiglaEstado());
        }
        clubeRepository.save(clube);
        return clube;
    }

    public Boolean inactivateClube(Long id) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new NotFoundException("Clube not found"));
        clube.setAtivo(false);
        clubeRepository.save(clube);
        return true;
    }

    public Page<Clube> listarClubes(FiltroClubeDto filtroClubeDto, Pageable pageable) {
        return clubeRepository.listByFilters(filtroClubeDto.getNome(), filtroClubeDto.getEstado(), filtroClubeDto.getSituacao(), pageable);
    }

    public Clube listarClubeById(Long id) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new NotFoundException("Clube not found"));
        return clube;
    }

    private Clube convert(ClubeDto clubeDto) {
        Clube clube = new Clube();
        clube.setNome(clubeDto.getNome());
        clube.setSiglaEstado(clubeDto.getSiglaEstado());
        clube.setDataCriacao(clubeDto.getDataCriacao());
        clube.setAtivo(clubeDto.getAtivo());
        return clube;
    }

}
