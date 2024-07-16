package com.meli.futebol.services;

import com.meli.futebol.dtos.ClubeDto;
import com.meli.futebol.dtos.FiltroClubeDto;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Clube;
import com.meli.futebol.repositories.ClubeRepository;
import com.meli.futebol.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    public Clube insertClube(ClubeDto clubeDto) {
        validaDados(clubeDto);
        boolean clubExists = clubeRepository.existsByNome(clubeDto.getNome(), clubeDto.getSiglaEstado());
        if (clubExists) {
            throw new DuplicateKeyException("Não foi possivel concluir o cadastro do clube, pois esses dados já existem no banco de dados.");
        }
        Clube clube = convert(clubeDto);
        clubeRepository.save(clube);
        return clube;
    }

    public Clube updateClube(Long id, ClubeDto clubeDto) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new NotFoundException("Clube not found"));
        validaDados(clubeDto);
        validaDataPartida(id, clubeDto.getDataCriacao());
        boolean clubExists = clubeRepository.existsByNomeAndSiglaEstadoIgnoringId(clubeDto.getNome(), clubeDto.getSiglaEstado(), id);
        if (clubExists) {
            throw new DuplicateKeyException("Não foi possível concluir a atualização do clube, pois esses dados já existem no banco de dados.");
        }
        clube.setDataCriacao(clubeDto.getDataCriacao());
        clube.setNome(clubeDto.getNome());
        clube.setSiglaEstado(clubeDto.getSiglaEstado());
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
        try {
            return clubeRepository.listByFilters(filtroClubeDto.getNome(), filtroClubeDto.getEstado(), filtroClubeDto.getSituacao(), pageable);
        } catch (InvalidInputException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    public Clube listarClubeById(Long id) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new NotFoundException("Clube not found"));
        return clube;
    }

    private void validaDados(ClubeDto clubeDto) {
        if (clubeDto.getNome().length() < 2) {
            throw new InvalidInputException("Invalid size name");
        }
        if (clubeDto.getNome().isEmpty() || clubeDto.getNome() == null) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (!isEstadoValido(clubeDto.getSiglaEstado())) {
            throw new InvalidInputException("Invalid state from Brazil");
        }
        if (clubeDto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Data cannot be after now");
        }
    }

    private void validaDataPartida(Long id, LocalDate dataHora) {
        int existePartida = partidaRepository.existsByDataHoraBeforeAndClubeIds(dataHora, id);
        if (existePartida == 1) {
            throw new InvalidInputException("the date cannot be after a played match date");
        }
    }

    private boolean isEstadoValido(String siglaEstado) {
        String[] estados = {"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        for (String estado : estados) {
            if (estado.equalsIgnoreCase(siglaEstado)) {
                return true;
            }
        }
        return false;
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
