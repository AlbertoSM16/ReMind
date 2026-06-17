package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.Mapper.PacienteMapper;
import com.remind.back.dto.PacienteCreatedDTO;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
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
class PacienteServiceImplTest {

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
    void testCreatePaciente_WithTerapeuta() {
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

        PacienteCreatedDTO result = pacienteService.createPaciente(inputDTO);

        assertNotNull(result);
        assertEquals("juan.perez", result.getUsuario());
        assertEquals("plainPass", result.getContrasenia());
        verify(agendaRepository).save(any(Agenda.class));
        verify(pacienteAgendaRepository).save(any(PacienteAgenda.class));
    }

    @Test
    void testCreatePaciente_WithoutTerapeuta() {
        PacienteInputDTO inputDTO = new PacienteInputDTO();
        inputDTO.setNombre("Juan");
        inputDTO.setApellido("Perez");

        Paciente pacienteMapeado = new Paciente();
        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setNombre("Juan");

        when(utils.generateRandomPassword("Juan", "Perez")).thenReturn("plainPass");
        when(passwordEncoder.encode("plainPass")).thenReturn("hashedPass");
        when(utils.generateRandomUsername("Juan", "Perez")).thenReturn("juan.perez");
        when(pacienteMapper.PacienteInputDTOToPaciente(inputDTO)).thenReturn(pacienteMapeado);
        when(pacienteRepository.save(pacienteMapeado)).thenReturn(pacienteGuardado);

        PacienteCreatedDTO result = pacienteService.createPaciente(inputDTO);

        assertNotNull(result);
        verify(agendaRepository, never()).save(any());
    }

    @Test
    void testGetAllPacientes_AsAdmin() {
        Paciente paciente = new Paciente();
        paciente.setNombre("Paciente1");
        PacienteOutputDTO output = new PacienteOutputDTO();

        when(pacienteRepository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(paciente)));
        when(pacienteMapper.PacienteToPacienteOutputDTO(paciente)).thenReturn(output);

        List<PacienteOutputDTO> result = pacienteService.getAllPacientes(0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPacienteById_Found() {
        Paciente paciente = new Paciente();
        paciente.setId(1);
        PacienteOutputDTO output = new PacienteOutputDTO();
        output.setId(1);

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(pacienteMapper.PacienteToPacienteOutputDTO(paciente)).thenReturn(output);

        PacienteOutputDTO result = pacienteService.getPacienteById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetPacienteById_NotFound() {
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> pacienteService.getPacienteById(99));
    }

    @Test
    void testDeletePaciente() {
        Integer pacienteId = 1;
        when(pacienteRepository.existsById(pacienteId)).thenReturn(true);

        Agenda agenda = new Agenda();
        agenda.setId(100);

        PacienteAgenda pacienteAgenda = new PacienteAgenda();
        pacienteAgenda.setAgenda(agenda);

        when(pacienteAgendaRepository.findByPacienteId(pacienteId))
                .thenReturn(Arrays.asList(pacienteAgenda));

        pacienteService.deletePaciente(pacienteId);

        verify(juegoAgendaRepository).deleteByAgendaId(100);
        verify(agendaTerapeutaRepository).deleteByAgendaId(100);
        verify(pacienteAgendaRepository).delete(pacienteAgenda);
        verify(agendaRepository).delete(agenda);
        verify(pacienteRepository).deleteById(pacienteId);
    }

    @Test
    void testDeletePaciente_NotFound() {
        when(pacienteRepository.existsById(99)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> pacienteService.deletePaciente(99));
        verify(pacienteRepository, never()).deleteById(any());
    }

    @Test
    void testUpdatePaciente() {
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

        pacienteService.updatePaciente(pacienteId, dto);

        assertEquals("Carlos Editado", pacienteExistente.getNombre());
        assertEquals("hashedNuevaPass", pacienteExistente.getContrasena());
        verify(pacienteRepository).save(pacienteExistente);
    }

    @Test
    void testUpdatePaciente_WithTerapeuta() {
        Integer pacienteId = 1;
        PacienteInputDTO dto = new PacienteInputDTO();
        dto.setTerapeuta_id(5);

        Paciente pacienteExistente = new Paciente();
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(5);

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        when(terapeutaRepository.findById(5)).thenReturn(Optional.of(terapeuta));
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteExistente);
        when(pacienteMapper.PacienteToPacienteOutputDTO(pacienteExistente)).thenReturn(new PacienteOutputDTO());

        pacienteService.updatePaciente(pacienteId, dto);

        assertEquals(terapeuta, pacienteExistente.getTerapeuta());
    }

    @Test
    void testUpdatePaciente_NotFound() {
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> pacienteService.updatePaciente(99, new PacienteInputDTO()));
    }

    @Test
    void testResetPassword() {
        Integer pacienteId = 1;
        Paciente paciente = new Paciente();
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(utils.generateRandomPassword("Juan", "Perez")).thenReturn("newPlainPass");
        when(passwordEncoder.encode("newPlainPass")).thenReturn("newHashedPass");

        PasswordResetDTO result = pacienteService.resetPassword(pacienteId);

        assertEquals("newPlainPass", result.getNuevaContrasenia());
        verify(pacienteRepository).save(paciente);
    }

    @Test
    void testResetPassword_PacienteNotFound() {
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> pacienteService.resetPassword(99));
    }
}
