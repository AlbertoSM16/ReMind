package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.remind.back.entities.Juego;
import com.remind.back.repositories.JuegoRepository;
import com.remind.back.services.JuegoService;

@ExtendWith(MockitoExtension.class)
public class JuegoServiceTest {

    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private JuegoService juegoService;

    @Test
    void testFindAll() {
        // Arrange (Preparar)
        Juego juego1 = new Juego();
        Juego juego2 = new Juego();
        when(juegoRepository.findAll()).thenReturn(Arrays.asList(juego1, juego2));

        // Act (Actuar)
        List<Juego> result = juegoService.findAll();

        // Assert (Afirmar)
        assertEquals(2, result.size());
        verify(juegoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Juego juego = new Juego();
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juego));

        // Act
        Optional<Juego> result = juegoService.findById(1);

        // Assert
        assertTrue(result.isPresent());
        verify(juegoRepository).findById(1);
    }

    @Test
    void testSave() {
        // Arrange
        Juego juego = new Juego();
        when(juegoRepository.save(juego)).thenReturn(juego);

        // Act
        Juego result = juegoService.save(juego);

        // Assert
        assertNotNull(result);
        verify(juegoRepository).save(juego);
    }

    @Test
    void testDeleteById() {
        // Act
        juegoService.deleteById(1);

        // Assert
        verify(juegoRepository, times(1)).deleteById(1);
    }
}