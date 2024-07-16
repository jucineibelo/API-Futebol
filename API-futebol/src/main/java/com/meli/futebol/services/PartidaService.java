package com.meli.futebol.services;

import com.meli.futebol.dtos.FiltroPartidaDto;
import com.meli.futebol.dtos.PartidaDto;
import com.meli.futebol.exceptions.DateInvalidException;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Partida;
import com.meli.futebol.repositories.ClubeRepository;
import com.meli.futebol.repositories.EstadioRepository;
import com.meli.futebol.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PartidaService {

    @Autowired
    PartidaRepository partidaRepository;

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    EstadioRepository estadioRepository;

    public Partida insertPartida(PartidaDto partidaDto) {
        validaClubesDiferentes(partidaDto);
        Partida partida = convertToEntity(partidaDto);
        verificaPlacarInvalido(partidaDto.getGolsClube1(), partidaDto.getGolsClube2());
        partida.setResultado(validaResultado(partidaDto));
        invalidaDataHoraFutura(partidaDto.getDataHora());
        validaDataCriacao(partidaDto);
        partidaRepository.save(partida);
        return partida;
    }

    private void validaDataCriacao(PartidaDto partidaDto) {
        LocalDate dataCriacao = partidaDto.getDataHora().toLocalDate();
        boolean invalidDateClube = clubeRepository.existsClubByDateInvalid(dataCriacao, partidaDto.getIdClube1());
        if (invalidDateClube) {
            throw new DateInvalidException("Você não pode cadastrar uma partida de um clube cadastrado posteriormente");
        }
        invalidDateClube = clubeRepository.existsClubByDateInvalid(dataCriacao, partidaDto.getIdClube2());
        if (invalidDateClube) {
            throw new DateInvalidException("Você não pode cadastrar uma partida de um clube cadastrado posteriormente");
        }
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

    private void validaClubesDiferentes(PartidaDto partidaDto) {
        if (partidaDto.getIdClube1().equals(partidaDto.getIdClube2())) {
            throw new InvalidInputException("Error, the clubes are the same");
        }
    }

    private void verificaPlacarInvalido(int golsClub1, int golsClub2) {
        if (golsClub1 < 0 || golsClub2 < 0) {
            throw new InvalidInputException("Invalid result");
        }
    }

    private String validaResultado(PartidaDto partidaDto) {
        String resultado = "";
        if (partidaDto.getGolsClube1() > partidaDto.getGolsClube2()) {
            resultado = "Time: " + partidaDto.getIdClube1() + " venceu por: " + partidaDto.getGolsClube1() + " contra: " + partidaDto.getGolsClube2();
        } else if (partidaDto.getGolsClube1() < partidaDto.getGolsClube2()) {
            resultado = "Time: " + partidaDto.getIdClube2() + " venceu por: " + partidaDto.getGolsClube2() + " contra: " + partidaDto.getGolsClube1();
        } else {
            resultado = "Empate! Time 1 : " + partidaDto.getGolsClube2() + " contra Time 2: " + partidaDto.getGolsClube1();
        }
        return resultado;
    }

    private void invalidaDataHoraFutura(LocalDateTime dataHora) {
        if (!dataHora.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("Invalid data hora");
        }
    }

    private Partida convertToEntity(PartidaDto partidaDto) {
        Partida partida = new Partida();
        partida.setClube1(clubeRepository.findById(partidaDto.getIdClube1()).orElseThrow(() -> new InvalidInputException("Clube casa not found")));
        partida.setClube2(clubeRepository.findById(partidaDto.getIdClube2()).orElseThrow(() -> new InvalidInputException("Clube visitante not found")));
        partida.setEstadio(estadioRepository.findById(partidaDto.getIdEstadio()).orElseThrow(() -> new InvalidInputException("Estadio not found")));
        partida.setGolsClube1(partidaDto.getGolsClube1());
        partida.setGolsClube2(partidaDto.getGolsClube2());
        partida.setDataHora(partidaDto.getDataHora());
        return partida;
    }

}
