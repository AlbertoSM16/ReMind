package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.remind.back.Mapper.AgendaMapper;
import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.Juego;
import com.remind.back.entities.JuegoAgenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.JuegoRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.AgendaService;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceImplTest {

    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private AgendaMapper agendaMapper;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private TerapeutaRepository terapeutaRepository;
    @Mock
    private PacienteAgendaRepository pacienteAgendaRepository;
    @Mock
    private AgendaTerapeutaRepository agendaTerapeutaRepository;
    @Mock
    private JuegoAgendaRepository juegoAgendaRepository;
    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    void testCreateAgenda() {
        // Arrange
        AgendaInputDTO inputDTO = new AgendaInputDTO();
        inputDTO.setPaciente_id(1);
        inputDTO.setTerapeuta_id(2);

        Agenda agenda = new Agenda();
        Agenda savedAgenda = new Agenda();
        savedAgenda.setId(100);

        Paciente paciente = new Paciente();
        Terapeuta terapeuta = new Terapeuta();

        AgendaOutputDTO outputDTO = new AgendaOutputDTO(100, "Agenda Test", "Paciente", "Terapeuta");

        when(agendaMapper.agendaInputDTOToAgenda(inputDTO)).thenReturn(agenda);
        when(agendaRepository.save(agenda)).thenReturn(savedAgenda);

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(terapeutaRepository.findById(2)).thenReturn(Optional.of(terapeuta));

        when(agendaMapper.agendaToAgendaOutputDTO(savedAgenda)).thenReturn(outputDTO);

        // Act
        AgendaOutputDTO result = agendaService.createAgenda(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getId());
        verify(pacienteAgendaRepository).save(any());
        verify(agendaTerapeutaRepository).save(any());
    }

    @Test
    void testAssignJuegoToAgenda() {
        // Arrange
        Integer agendaId = 1;
        Integer juegoId = 10;
        Integer dificultad = 3;

        Agenda agenda = new Agenda();
        Juego juego = new Juego();

        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId)).thenReturn(Optional.empty());
        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));

        // Act
        agendaService.assignJuegoToAgenda(agendaId, juegoId, dificultad);

        // Assert
        verify(juegoAgendaRepository).save(any(JuegoAgenda.class));
    }

    @Test
    void testAssignJuegoToAgenda_ThrowsIllegalStateExceptionIfAlreadyAssigned() {
        // Arrange
        Integer agendaId = 1;
        Integer juegoId = 10;

        // Simulamos que ya existe una asignación
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId))
                .thenReturn(Optional.of(new JuegoAgenda()));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            agendaService.assignJuegoToAgenda(agendaId, juegoId, 3);
        });

        assertEquals("Este juego ya ha sido asignado a la agenda.", exception.getMessage());
        verify(juegoAgendaRepository, never()).save(any(JuegoAgenda.class));
    }

    @Test
    void testCompletarJuego() {
        // Arrange
        Integer agendaId = 1;
        Integer juegoId = 10;
        JuegoAgenda asignacion = new JuegoAgenda();
        asignacion.setRealizado(false);

        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId))
                .thenReturn(Optional.of(asignacion));

        // Act
        agendaService.completarJuego(agendaId, juegoId);

        // Assert
        assertTrue(asignacion.getRealizado());
        assertNotNull(asignacion.getFechaRealizacion());
        verify(juegoAgendaRepository).save(asignacion);
    }
}