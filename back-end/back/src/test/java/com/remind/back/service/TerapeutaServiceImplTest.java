package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.Mapper.TerapeutaMapper;
import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.PacienteService;
import com.remind.back.services.TerapeutaService;
import com.remind.back.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class TerapeutaServiceImplTest {

    @Mock
    private TerapeutaRepository terapeutaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private PacienteService pacienteService;
    @Mock
    private TerapeutaMapper terapeutaMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Utils utils;

    @InjectMocks
    private TerapeutaService terapeutaService;

    @Test
    void testCreateTerapeuta() {
        // Arrange
        TerapeutaInputDTO inputDTO = new TerapeutaInputDTO();
        inputDTO.setNombre("Ana");
        inputDTO.setApellido("Gomez");

        Terapeuta terapeutaMapeado = new Terapeuta();

        when(utils.generateRandomPassword("Ana", "Gomez")).thenReturn("pass123");
        when(passwordEncoder.encode("pass123")).thenReturn("hashedPass");
        when(utils.generateRandomUsername("Ana", "Gomez")).thenReturn("ana.gomez");
        when(terapeutaMapper.toTerapeuta(inputDTO)).thenReturn(terapeutaMapeado);
        when(terapeutaRepository.save(terapeutaMapeado)).thenReturn(terapeutaMapeado);

        // Act
        TerapeutaCreatedDTO result = terapeutaService.createTerapeuta(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ana.gomez", result.getUsuario());
        assertEquals("pass123", result.getContrasena());
        verify(terapeutaRepository, times(1)).save(terapeutaMapeado);
    }

    @Test
    void testDeleteTerapeuta() {
        // Arrange
        int terapeutaId = 1;
        when(terapeutaRepository.existsById(terapeutaId)).thenReturn(true);

        Paciente paciente1 = new Paciente();
        paciente1.setId(10);
        Paciente paciente2 = new Paciente();
        paciente2.setId(20);

        // Simulamos que el terapeuta tiene 2 pacientes asociados
        when(pacienteRepository.findByTerapeutaId(terapeutaId)).thenReturn(Arrays.asList(paciente1, paciente2));

        // Act
        terapeutaService.deleteTerapeuta(terapeutaId);

        // Assert
        // Verificamos que se llame al borrado de los pacientes asociados
        verify(pacienteService, times(1)).deletePaciente(10);
        verify(pacienteService, times(1)).deletePaciente(20);
        // Verificamos que finalmente se borre el terapeuta
        verify(terapeutaRepository, times(1)).deleteById(terapeutaId);
    }

    @Test
    void testDeleteTerapeuta_ThrowsExceptionWhenNotExists() {
        // Arrange
        int terapeutaId = 99;
        when(terapeutaRepository.existsById(terapeutaId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            terapeutaService.deleteTerapeuta(terapeutaId);
        });

        assertEquals("No existe un terapeuta con ese id y no se puede eliminar", exception.getMessage());
        verify(pacienteService, never()).deletePaciente(anyInt());
        verify(terapeutaRepository, never()).deleteById(anyInt());
    }
}