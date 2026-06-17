package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.remind.back.Mapper.AgendaMapper;
import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.JuegoAsignadoDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.Juego;
import com.remind.back.entities.JuegoAgenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.JuegoRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.AgendaServiceImpl;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

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
    private AgendaServiceImpl agendaService;

    @BeforeEach
    void setUpSecurityContext() {
        Authentication auth = mock(Authentication.class);
        lenient().when(auth.getName()).thenReturn("admin");
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        lenient().doReturn(authorities).when(auth).getAuthorities();
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateAgenda() {
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

        AgendaOutputDTO result = agendaService.createAgenda(inputDTO);

        assertNotNull(result);
        assertEquals(100, result.getId());
        verify(pacienteAgendaRepository).save(any());
        verify(agendaTerapeutaRepository).save(any());
    }

    @Test
    void testCreateAgenda_WithoutPaciente() {
        AgendaInputDTO inputDTO = new AgendaInputDTO();
        Agenda agenda = new Agenda();
        Agenda savedAgenda = new Agenda();
        savedAgenda.setId(200);
        AgendaOutputDTO outputDTO = new AgendaOutputDTO(200, "Solo Agenda", null, null);

        when(agendaMapper.agendaInputDTOToAgenda(inputDTO)).thenReturn(agenda);
        when(agendaRepository.save(agenda)).thenReturn(savedAgenda);
        when(agendaMapper.agendaToAgendaOutputDTO(savedAgenda)).thenReturn(outputDTO);

        AgendaOutputDTO result = agendaService.createAgenda(inputDTO);

        assertNotNull(result);
        assertEquals(200, result.getId());
        verify(pacienteAgendaRepository, never()).save(any());
        verify(agendaTerapeutaRepository, never()).save(any());
    }

    @Test
    void testGetAgendaById_Found() {
        Agenda agenda = new Agenda();
        agenda.setId(1);
        AgendaOutputDTO output = new AgendaOutputDTO(1, "Agenda1", "Paciente1", "Terapeuta1");

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));
        when(agendaMapper.agendaToAgendaOutputDTO(agenda)).thenReturn(output);

        AgendaOutputDTO result = agendaService.getAgendaById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetAgendaById_NotFound() {
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> agendaService.getAgendaById(99));
    }

    @Test
    void testGetAllAgendas() {
        Agenda agenda = new Agenda();
        AgendaOutputDTO output = new AgendaOutputDTO(1, "Agenda1", "P", "T");
        when(agendaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(agenda)));
        when(agendaMapper.agendaToAgendaOutputDTO(agenda)).thenReturn(output);

        List<AgendaOutputDTO> result = agendaService.getAllAgendas(0, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteAgenda_Success() {
        when(agendaRepository.existsById(1)).thenReturn(true);

        agendaService.deleteAgenda(1);

        verify(agendaRepository).deleteById(1);
        verify(pacienteAgendaRepository).deleteByAgendaId(1);
        verify(agendaTerapeutaRepository).deleteByAgendaId(1);
    }

    @Test
    void testDeleteAgenda_NotFound() {
        when(agendaRepository.existsById(99)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> agendaService.deleteAgenda(99));
        verify(agendaRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateAgenda_Success() {
        AgendaInputDTO inputDTO = new AgendaInputDTO();
        inputDTO.setNombre("Actualizada");

        Agenda existingAgenda = new Agenda();
        existingAgenda.setId(1);
        existingAgenda.setNombre("Original");

        AgendaOutputDTO output = new AgendaOutputDTO(1, "Actualizada", "P", "T");

        when(agendaRepository.findById(1)).thenReturn(Optional.of(existingAgenda));
        when(agendaRepository.save(existingAgenda)).thenReturn(existingAgenda);
        when(agendaMapper.agendaToAgendaOutputDTO(existingAgenda)).thenReturn(output);

        AgendaOutputDTO result = agendaService.updateAgenda(1, inputDTO);

        assertEquals("Actualizada", existingAgenda.getNombre());
        assertNotNull(result);
    }

    @Test
    void testUpdateAgenda_NotFound() {
        AgendaInputDTO inputDTO = new AgendaInputDTO();
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> agendaService.updateAgenda(99, inputDTO));
    }

    @Test
    void testAssignJuegoToAgenda() {
        Integer agendaId = 1;
        Integer juegoId = 10;
        Integer dificultad = 3;

        Agenda agenda = new Agenda();
        Juego juego = new Juego();

        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId)).thenReturn(Optional.empty());
        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));

        agendaService.assignJuegoToAgenda(agendaId, juegoId, dificultad);

        verify(juegoAgendaRepository).save(any(JuegoAgenda.class));
    }

    @Test
    void testAssignJuegoToAgenda_AlreadyAssigned() {
        Integer agendaId = 1;
        Integer juegoId = 10;

        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId))
                .thenReturn(Optional.of(new JuegoAgenda()));

        assertThrows(IllegalStateException.class,
                () -> agendaService.assignJuegoToAgenda(agendaId, juegoId, 3));
        verify(juegoAgendaRepository, never()).save(any(JuegoAgenda.class));
    }

    @Test
    void testAssignJuegoToAgenda_AgendaNotFound() {
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(1, 1)).thenReturn(Optional.empty());
        when(agendaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> agendaService.assignJuegoToAgenda(1, 1, 1));
    }

    @Test
    void testAssignJuegoToAgenda_JuegoNotFound() {
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(1, 99)).thenReturn(Optional.empty());
        when(agendaRepository.findById(1)).thenReturn(Optional.of(new Agenda()));
        when(juegoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> agendaService.assignJuegoToAgenda(1, 99, 1));
    }

    @Test
    void testRemoveJuegoFromAgenda_Success() {
        JuegoAgenda asignacion = new JuegoAgenda();
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(1, 1)).thenReturn(Optional.of(asignacion));

        agendaService.removeJuegoFromAgenda(1, 1);

        verify(juegoAgendaRepository).delete(asignacion);
    }

    @Test
    void testRemoveJuegoFromAgenda_NotFound() {
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(1, 99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> agendaService.removeJuegoFromAgenda(1, 99));
    }

    @Test
    void testGetJuegosByAgendaId_Success() {
        Juego juego = new Juego();
        juego.setId(1);
        juego.setNombre("Memory");
        juego.setCodigo("MEM01");

        JuegoAgenda ja = new JuegoAgenda();
        ja.setJuego(juego);
        ja.setDificultad(2);
        ja.setRealizado(false);

        when(agendaRepository.existsById(1)).thenReturn(true);
        when(juegoAgendaRepository.findByAgendaId(1)).thenReturn(List.of(ja));

        List<JuegoAsignadoDTO> result = agendaService.getJuegosByAgendaId(1);

        assertEquals(1, result.size());
        assertEquals("Memory", result.get(0).getNombre());
    }

    @Test
    void testGetJuegosByAgendaId_AgendaNotFound() {
        when(agendaRepository.existsById(99)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> agendaService.getJuegosByAgendaId(99));
    }

    @Test
    void testGetJuegosByPacienteId_Success() {
        Integer pacienteId = 1;
        Agenda agenda = new Agenda();
        agenda.setId(10);

        PacienteAgenda pa = new PacienteAgenda();
        pa.setAgenda(agenda);

        Juego juego = new Juego();
        juego.setId(1);
        juego.setNombre("Memory");
        juego.setCodigo("MEM01");

        JuegoAgenda ja = new JuegoAgenda();
        ja.setJuego(juego);
        ja.setDificultad(2);
        ja.setRealizado(false);

        when(pacienteAgendaRepository.findByPacienteId(pacienteId))
                .thenReturn(List.of(pa));
        when(juegoAgendaRepository.findByAgendaId(10)).thenReturn(List.of(ja));

        Map<String, Object> result = agendaService.getJuegosByPacienteId(pacienteId);

        assertEquals(10, result.get("agendaId"));
        assertNotNull(result.get("juegos"));
    }

    @Test
    void testGetJuegosByPacienteId_NoAgenda() {
        when(pacienteAgendaRepository.findByPacienteId(99)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class,
                () -> agendaService.getJuegosByPacienteId(99));
    }

    @Test
    void testCompletarJuego() {
        Integer agendaId = 1;
        Integer juegoId = 10;
        JuegoAgenda asignacion = new JuegoAgenda();
        asignacion.setRealizado(false);

        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId))
                .thenReturn(Optional.of(asignacion));

        agendaService.completarJuego(agendaId, juegoId);

        assertTrue(asignacion.getRealizado());
        assertNotNull(asignacion.getFechaRealizacion());
        verify(juegoAgendaRepository).save(asignacion);
    }

    @Test
    void testCompletarJuego_NotFound() {
        when(juegoAgendaRepository.findByAgendaIdAndJuegoId(1, 99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> agendaService.completarJuego(1, 99));
    }

}
