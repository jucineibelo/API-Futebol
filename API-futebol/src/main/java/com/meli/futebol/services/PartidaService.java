package com.meli.futebol.services;

import com.meli.futebol.dtos.FiltroPartidaDto;
import com.meli.futebol.dtos.PartidaDto;
import com.meli.futebol.exceptions.ConflitException;
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
        Partida partida = validacoesInsert(partidaDto);
        partidaRepository.save(partida);
        return partida;
    }

    public Partida updatePartida(Long id, PartidaDto partidaDto) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new NotFoundException("Partida not found"));
        validacoesUpdate(partidaDto, partida);
        partida.setResultado(validaResultado(partidaDto));
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

    public Page<Partida> listarPartidas(FiltroPartidaDto filtroPartidaDto, Pageable pageable) {
        return partidaRepository.findByFilters(filtroPartidaDto.getNomeClube(), filtroPartidaDto.getNomeEstadio(), pageable);
    }

    private void invalidaClubesIguais(PartidaDto partidaDto) {
        if (partidaDto.getIdClube1().equals(partidaDto.getIdClube2())) {
            throw new InvalidInputException("Error, the clubes are the same");
        }
    }

    private void verificaPlacarInvalido(int golsClub1, int golsClub2) {
        if (golsClub1 < 0 || golsClub2 < 0) {
            throw new InvalidInputException("Invalid result, goal difference less than zero");
        }
    }

    private String validaResultado(PartidaDto partidaDto) {
        String resultado = "";
        if (partidaDto.getGolsClube1() > partidaDto.getGolsClube2()) {
            resultado = "Club: " + partidaDto.getIdClube1() + " win by: " + partidaDto.getGolsClube1() + " against: " + partidaDto.getGolsClube2();
        } else if (partidaDto.getGolsClube1() < partidaDto.getGolsClube2()) {
            resultado = "Club: " + partidaDto.getIdClube2() + " win by: " + partidaDto.getGolsClube2() + " against: " + partidaDto.getGolsClube1();
        } else {
            resultado = "Draw! Club 1 : " + partidaDto.getGolsClube2() + " against Club 2: " + partidaDto.getGolsClube1();
        }
        return resultado;
    }

    private void invalidaDataHoraFutura(LocalDateTime dataHora) {
        if (!dataHora.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("Invalid data hora");
        }
    }

    private void verificaClubeInativo(PartidaDto partidaDto) {
        boolean clube1Ativo = clubeRepository.existsClubByAtivo(partidaDto.getIdClube1());
        boolean clube2Ativo = clubeRepository.existsClubByAtivo(partidaDto.getIdClube2());
        if (!clube1Ativo || !clube2Ativo) {
            throw new ConflitException("It is not possible to play with one or more inactive clubs");
        }
    }

    private void validaDataCriacao(PartidaDto partidaDto) {
        LocalDate dataCriacao = partidaDto.getDataHora().toLocalDate();
        if (clubeRepository.existsClubByDateInvalid(dataCriacao, partidaDto.getIdClube1())) {
            throw new DateInvalidException("You cannot register a match for a later registered club");
        }
        if (clubeRepository.existsClubByDateInvalid(dataCriacao, partidaDto.getIdClube2())) {
            throw new DateInvalidException("You cannot register a match for a later registered club");
        }
    }

    private void existePartidaProgramada(PartidaDto partidaDto) {
        if (partidaRepository.existsMatchesByClub(partidaDto.getIdClube1(), partidaDto.getIdClube2(), partidaDto.getDataHora()) == 1) {
            throw new DateInvalidException("There is a game within 48 hours for one of the clubs");
        }
    }

    private void existePartidaByEStadio(PartidaDto partidaDto) {
        if (partidaRepository.existsMatchesByEstadio(partidaDto.getIdEstadio(), partidaDto.getDataHora()) == 1) {
            throw new ConflitException("This estadio will be occupied on this date.");
        }
    }

    private void clubeOrEstadioExists(PartidaDto partidaDto, Partida partida) {
        partida.setClube1(clubeRepository.findById(partidaDto.getIdClube1()).orElseThrow(() -> new InvalidInputException("Clube casa not found")));
        partida.setClube2(clubeRepository.findById(partidaDto.getIdClube2()).orElseThrow(() -> new InvalidInputException("Clube visitante not found")));
        partida.setEstadio(estadioRepository.findById(partidaDto.getIdEstadio()).orElseThrow(() -> new InvalidInputException("Estadio not found")));
    }

    private Partida validacoesInsert(PartidaDto partidaDto) {
        invalidaClubesIguais(partidaDto);
        Partida partida = convertToEntity(partidaDto);
        verificaPlacarInvalido(partidaDto.getGolsClube1(), partidaDto.getGolsClube2());
        partida.setResultado(validaResultado(partidaDto));
        invalidaDataHoraFutura(partidaDto.getDataHora());
        validaDataCriacao(partidaDto);
        verificaClubeInativo(partidaDto);
        existePartidaProgramada(partidaDto);
        existePartidaByEStadio(partidaDto);
        return partida;
    }

    private void validacoesUpdate(PartidaDto partidaDto, Partida partida) {
        invalidaClubesIguais(partidaDto);
        clubeOrEstadioExists(partidaDto, partida);
        verificaPlacarInvalido(partidaDto.getGolsClube1(), partidaDto.getGolsClube2());
        invalidaDataHoraFutura(partidaDto.getDataHora());
        validaDataCriacao(partidaDto);
        verificaClubeInativo(partidaDto);
        existePartidaProgramada(partidaDto);
        existePartidaByEStadio(partidaDto);
    }

    private Partida convertToEntity(PartidaDto partidaDto) {
        Partida partida = new Partida();
        clubeOrEstadioExists(partidaDto, partida);
        partida.setGolsClube1(partidaDto.getGolsClube1());
        partida.setGolsClube2(partidaDto.getGolsClube2());
        partida.setDataHora(partidaDto.getDataHora());
        return partida;
    }

}
