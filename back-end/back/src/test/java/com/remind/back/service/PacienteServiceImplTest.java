package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.Mapper.PacienteMapper;
import com.remind.back.dto.PacienteCreatedDTO;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.PacienteServiceImpl;
import com.remind.back.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private PacienteAgendaRepository pacienteAgendaRepository;
    @Mock
    private JuegoAgendaRepository juegoAgendaRepository;
    @Mock
    private AgendaTerapeutaRepository agendaTerapeutaRepository;
    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private TerapeutaRepository terapeutaRepository;
    @Mock
    private PacienteMapper pacienteMapper;
    @Mock
    private Utils utils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    void testCreatePaciente_WithTerapeuta() {
        // Arrange
        PacienteInputDTO inputDTO = new PacienteInputDTO();
        inputDTO.setNombre("Juan");
        inputDTO.setApellido("Perez");
        inputDTO.setTerapeuta_id(1);

        Paciente pacienteMapeado = new Paciente();
        pacienteMapeado.setNombre("Juan");

        Terapeuta terapeuta = new Terapeuta();

        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setNombre("Juan");
        pacienteGuardado.setTerapeuta(terapeuta);
        when(utils.generateRandomPassword("Juan", "Perez")).thenReturn("plainPass");
        when(passwordEncoder.encode("plainPass")).thenReturn("hashedPass");
        when(utils.generateRandomUsername("Juan", "Perez")).thenReturn("juan.perez");

        when(pacienteMapper.PacienteInputDTOToPaciente(inputDTO)).thenReturn(pacienteMapeado);
        when(terapeutaRepository.findById(1)).thenReturn(Optional.of(terapeuta));
        when(pacienteRepository.save(pacienteMapeado)).thenReturn(pacienteGuardado);

        when(agendaRepository.save(any(Agenda.class))).thenReturn(new Agenda());

        // Act
        PacienteCreatedDTO result = pacienteService.createPaciente(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("juan.perez", result.getUsuario());
        assertEquals("plainPass", result.getContrasenia());

        verify(agendaRepository).save(any(Agenda.class));
        verify(pacienteAgendaRepository).save(any(PacienteAgenda.class));
    }

    @Test
    void testDeletePaciente() {
        // Arrange
        Integer pacienteId = 1;
        when(pacienteRepository.existsById(pacienteId)).thenReturn(true);

        Agenda agenda = new Agenda();
        agenda.setId(100);

        PacienteAgenda pacienteAgenda = new PacienteAgenda();
        pacienteAgenda.setAgenda(agenda);

        when(pacienteAgendaRepository.findByPacienteId(pacienteId))
                .thenReturn(Arrays.asList(pacienteAgenda));

        // Act
        pacienteService.deletePaciente(pacienteId);

        // Assert
        // Debe borrar los datos en cascada de la agenda
        verify(juegoAgendaRepository).deleteByAgendaId(100);
        verify(agendaTerapeutaRepository).deleteByAgendaId(100);
        verify(pacienteAgendaRepository).delete(pacienteAgenda);
        verify(agendaRepository).delete(agenda);

        // Finalmente borra al paciente
        verify(pacienteRepository).deleteById(pacienteId);
    }

    @Test
    void testUpdatePaciente() {
        // Arrange
        Integer pacienteId = 1;
        PacienteInputDTO dto = new PacienteInputDTO();
        dto.setNombre("Carlos Editado");
        dto.setContrasena("nuevaPass");

        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setNombre("Carlos");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        when(passwordEncoder.encode("nuevaPass")).thenReturn("hashedNuevaPass");
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteExistente);
        when(pacienteMapper.PacienteToPacienteOutputDTO(pacienteExistente)).thenReturn(new PacienteOutputDTO());

        // Act
        pacienteService.updatePaciente(pacienteId, dto);

        // Assert
        assertEquals("Carlos Editado", pacienteExistente.getNombre());
        assertEquals("hashedNuevaPass", pacienteExistente.getContrasena());
        verify(pacienteRepository).save(pacienteExistente);
    }
}