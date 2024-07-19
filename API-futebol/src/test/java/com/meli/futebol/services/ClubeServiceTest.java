package com.meli.futebol.services;

import com.meli.futebol.dtos.ClubeDto;
import com.meli.futebol.dtos.FiltroClubeDto;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Clube;
import com.meli.futebol.repositories.ClubeRepository;
import com.meli.futebol.repositories.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    @Mock
    private ClubeRepository clubeRepository;
    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private ClubeService clubeService;
    private ClubeDto validClubeDto;
    private Clube validClube;

    @BeforeEach
    void setUp() {
        validClubeDto = new ClubeDto();
        validClubeDto.setId(1L);
        validClubeDto.setNome("Clube Teste");
        validClubeDto.setSiglaEstado("SP");
        validClubeDto.setDataCriacao(LocalDate.of(2000, 1, 1));
        validClubeDto.setAtivo(true);

        validClube = new Clube();
        validClube.setId(1L);
        validClube.setNome("Clube Teste");
        validClube.setSiglaEstado("SP");
        validClube.setDataCriacao(LocalDate.of(2000, 1, 1));
        validClube.setAtivo(true);
    }

    @Test
    void testInsertClube_Success() {
        when(clubeRepository.existsByNome(validClubeDto.getNome(), validClubeDto.getSiglaEstado())).thenReturn(false);
        when(clubeRepository.save(any(Clube.class))).thenReturn(validClube);
        Clube result = clubeService.insertClube(validClubeDto);
        assertNotNull(result);
        assertEquals(validClubeDto.getNome(), result.getNome());
        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void testInsertClube_DuplicateKeyException() {
        when(clubeRepository.existsByNome(validClubeDto.getNome(), validClubeDto.getSiglaEstado())).thenReturn(true);
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> {
            clubeService.insertClube(validClubeDto);
        });
        assertEquals("Não foi possivel concluir o cadastro do clube, pois esses dados já existem no banco de dados.", exception.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInsertClube_InvalidSizeName() {
        ClubeDto invalidClubeDto = new ClubeDto();
        invalidClubeDto.setNome("J");
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.insertClube(invalidClubeDto);
        });
        assertEquals("Invalid size name", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInsertClube_NomeVazio() {
        ClubeDto invalidClubeDto = new ClubeDto();
        invalidClubeDto.setNome("");
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.insertClube(invalidClubeDto);
        });
        assertEquals("Name cannot be empty", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInsertClube_NomeNulo() {
        ClubeDto invalidClubeDto = new ClubeDto();
        invalidClubeDto.setNome(null);
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.insertClube(invalidClubeDto);
        });
        assertEquals("Name cannot be empty", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInsertClube_InvalidEstado() {
        ClubeDto invalidClubeDto = new ClubeDto();
        invalidClubeDto.setNome("Testemunha");
        invalidClubeDto.setSiglaEstado("US");
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.insertClube(invalidClubeDto);
        });
        assertEquals("Invalid state from Brazil", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInsertClube_DataInvalida() {
        ClubeDto invalidClubeDto = new ClubeDto();
        invalidClubeDto.setNome("Testemunha");
        invalidClubeDto.setSiglaEstado("SC");
        invalidClubeDto.setDataCriacao(LocalDate.of(2024, 10, 1));

        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.insertClube(invalidClubeDto);
        });
        assertEquals("Data cannot be after now", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }


    @Test
    void testUpdateClube_Success() {
        when(partidaRepository.existsByDataHoraBeforeAndClubeIds(LocalDate.of(2000, 1, 1), 1L)).thenReturn(0);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(validClube));
        when(clubeRepository.existsByNomeAndSiglaEstadoIgnoringId(validClubeDto.getNome(), validClubeDto.getSiglaEstado(), 1L)).thenReturn(false);
        when(clubeRepository.save(any(Clube.class))).thenReturn(validClube);

        Clube result = clubeService.updateClube(1L, validClubeDto);

        assertNotNull(result);
        assertEquals(validClubeDto.getNome(), result.getNome());
        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void testUpdateClube_NotFoundException() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            clubeService.updateClube(1L, validClubeDto);
        });

        assertEquals("Clube not found", exception.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testUpdateClube_DuplicateKeyException() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(validClube));
        when(clubeRepository.existsByNomeAndSiglaEstadoIgnoringId(validClubeDto.getNome(), validClubeDto.getSiglaEstado(), 1L)).thenReturn(true);

        DuplicateKeyException e = assertThrows(DuplicateKeyException.class, () -> {
            clubeService.updateClube(1L, validClubeDto);
        });

        assertEquals("Não foi possível concluir a atualização do clube, pois esses dados já existem no banco de dados.", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testUpdateClube_verificaPartidaInvalida() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(validClube));
        when(partidaRepository.existsByDataHoraBeforeAndClubeIds(LocalDate.of(2000, 1, 1), 1L)).thenReturn(1);

        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            clubeService.updateClube(1L, validClubeDto);
        });

        assertEquals("the date cannot be after a played match date", e.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testInactivateClube_Success() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(validClube));

        Boolean result = clubeService.inactivateClube(1L);

        assertTrue(result);
        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void testInactivateClube_NotFoundException() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            clubeService.inactivateClube(1L);
        });

        assertEquals("Clube not found", exception.getMessage());
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void testListarClubes_Success() {
        FiltroClubeDto filtroClubeDto = new FiltroClubeDto();
        filtroClubeDto.setNome("Clube Teste");
        filtroClubeDto.setEstado("SP");
        filtroClubeDto.setSituacao(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> clubePage = new PageImpl<>(Arrays.asList(validClube), pageable, 1);
        when(clubeRepository.listByFilters(any(), any(), any(), eq(pageable))).thenReturn(clubePage);

        Page<Clube> result = clubeService.listarClubes(filtroClubeDto, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clubeRepository, times(1)).listByFilters(any(), any(), any(), eq(pageable));
    }

    @Test
    void testListarClubeById_Success() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(validClube));

        Clube result = clubeService.listarClubeById(1L);

        assertNotNull(result);
        assertEquals(validClube.getNome(), result.getNome());
        verify(clubeRepository, times(1)).findById(1L);
    }

    @Test
    void testListarClubeById_NotFoundException() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            clubeService.listarClubeById(1L);
        });

        assertEquals("Clube not found", exception.getMessage());
        verify(clubeRepository, times(1)).findById(1L);
    }
}
