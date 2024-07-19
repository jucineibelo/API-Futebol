package com.meli.futebol.services;

import com.meli.futebol.dtos.EstadioDto;
import com.meli.futebol.exceptions.InvalidInputException;
import com.meli.futebol.exceptions.NotFoundException;
import com.meli.futebol.models.Clube;
import com.meli.futebol.models.Estadio;
import com.meli.futebol.repositories.EstadioRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;
    private EstadioDto validEstadioDto;
    private Estadio validEstadio;

    @BeforeEach
    void setUp() {
        validEstadioDto = new EstadioDto();
        validEstadioDto.setNome("Estadio Teste");

        validEstadio = new Estadio();
        validEstadio.setId(1L);
        validEstadio.setNome("Estadio Teste");
    }

    @Test
    void testInsertEstadio_Success() {
        when(estadioRepository.existsByNome(validEstadio.getNome())).thenReturn(false);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(validEstadio);
        Estadio result = estadioService.insertEstadio(validEstadioDto);
        assertNotNull(result);
        assertEquals(validEstadio.getNome(), result.getNome());
        verify(estadioRepository, times(1)).save(any(Estadio.class));
    }

    @Test
    void testInsertEstadio_DuplicateKeyException() {
        when(estadioRepository.existsByNome(validEstadio.getNome())).thenReturn(true);
        DuplicateKeyException e = assertThrows(DuplicateKeyException.class, () -> estadioService.insertEstadio(validEstadioDto));

        assertEquals("Error inserting estadio, because it already exists", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testInsertEstadio_InvalidSize() {
        EstadioDto invalidEstadioDto = new EstadioDto();
        invalidEstadioDto.setNome("tt");

        InvalidInputException e = assertThrows(
                InvalidInputException.class, () -> estadioService.insertEstadio(invalidEstadioDto)
        );
        assertEquals("Invalid size name or empty", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testInsertEstadio_NameNull() {
        EstadioDto invalidEstadioDto = new EstadioDto();
        invalidEstadioDto.setNome(null);

        InvalidInputException e = assertThrows(
                InvalidInputException.class, () -> estadioService.insertEstadio(invalidEstadioDto)
        );
        assertEquals("Invalid size name or empty", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testInsertEstadio_EmptySize() {
        EstadioDto invalidEstadioDto = new EstadioDto();
        invalidEstadioDto.setNome("");

        InvalidInputException e = assertThrows(
                InvalidInputException.class, () -> estadioService.insertEstadio(invalidEstadioDto)
        );
        assertEquals("Invalid size name or empty", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testUpdateEstadio_Success() {
        when(estadioRepository.existsByNome(validEstadio.getNome())).thenReturn(false);
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(validEstadio));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(validEstadio);
        Estadio result = estadioService.updateEstadio(1L, validEstadioDto);
        assertNotNull(result);
        assertEquals(validEstadio.getNome(), result.getNome());
        verify(estadioRepository, times(1)).save(any(Estadio.class));
    }

    @Test
    void testUpdateEstadio_NotFound() {
        when(estadioRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> estadioService.updateEstadio(1L, validEstadioDto));
        assertEquals("Estadio not found", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testUpdateEstadio_DuplicateKeyException() {
        when(estadioRepository.existsByNome(validEstadio.getNome())).thenReturn(true);
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(validEstadio));
        DuplicateKeyException e = assertThrows(
                DuplicateKeyException.class, () -> estadioService.updateEstadio(1L, validEstadioDto)
        );
        assertEquals("Error updated estadio, because it already exists", e.getMessage());
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void testgetEstadioById_Success() {
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(validEstadio));
        Estadio result = estadioService.getEstadioById(1L);
        assertNotNull(result);
        assertEquals(validEstadio.getNome(), result.getNome());
        verify(estadioRepository, times(1)).findById(1L);
    }

    @Test
    void testgetEstadioList_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Estadio> estadioPage = new PageImpl<>(Arrays.asList(validEstadio), pageable, 1);
        when(estadioRepository.findByNome(any(), eq(pageable))).thenReturn(estadioPage);

        Page<Estadio> result = estadioService.getEstadioList(validEstadioDto, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(estadioRepository, times(1)).findByNome(any(), eq(pageable));
    }
}
