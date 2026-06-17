package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.remind.back.Mapper.TerapeutaMapper;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.dto.TerapeutaSeguimientoDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.AgendaTerapeuta;
import com.remind.back.entities.Juego;
import com.remind.back.entities.JuegoAgenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.PacienteTerapeutaRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.PacienteService;
import com.remind.back.services.TerapeutaServiceImpl;
import com.remind.back.utils.Utils;

@ExtendWith(MockitoExtension.class)
class TerapeutaServiceImplTest {

    @Mock
    private TerapeutaRepository terapeutaRepository;
    @Mock
    private PacienteTerapeutaRepository pacienteTerapeutaRepository;
    @Mock
    private JuegoAgendaRepository juegoAgendaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private PacienteService pacienteService;
    @Mock
    private PacienteAgendaRepository pacienteAgendaRepository;
    @Mock
    private TerapeutaMapper terapeutaMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Utils utils;

    @InjectMocks
    private TerapeutaServiceImpl terapeutaService;

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
    void testCreateTerapeuta() {
        TerapeutaInputDTO inputDTO = new TerapeutaInputDTO();
        inputDTO.setNombre("Ana");
        inputDTO.setApellido("Gomez");

        Terapeuta terapeutaMapeado = new Terapeuta();

        when(utils.generateRandomPassword("Ana", "Gomez")).thenReturn("pass123");
        when(passwordEncoder.encode("pass123")).thenReturn("hashedPass");
        when(utils.generateRandomUsername("Ana", "Gomez")).thenReturn("ana.gomez");
        when(terapeutaMapper.toTerapeuta(inputDTO)).thenReturn(terapeutaMapeado);
        when(terapeutaRepository.save(terapeutaMapeado)).thenReturn(terapeutaMapeado);

        TerapeutaCreatedDTO result = terapeutaService.createTerapeuta(inputDTO);

        assertNotNull(result);
        assertEquals("ana.gomez", result.getUsuario());
        assertEquals("pass123", result.getContrasena());
        verify(terapeutaRepository, times(1)).save(terapeutaMapeado);
    }

    @Test
    void testGetTerapeutaById_Found() {
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1);
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();
        output.setId(1);

        when(terapeutaRepository.findById(1)).thenReturn(Optional.of(terapeuta));
        when(terapeutaMapper.toDTO(terapeuta)).thenReturn(output);

        TerapeutaOutputDTO result = terapeutaService.getTerapeutaById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetTerapeutaById_NotFound() {
        when(terapeutaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.getTerapeutaById(99));
    }

    @Test
    void testGetAllTerapeutas() {
        Terapeuta terapeuta = new Terapeuta();
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();
        when(terapeutaRepository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(terapeuta)));
        when(terapeutaMapper.toDTO(terapeuta)).thenReturn(output);

        List<TerapeutaOutputDTO> result = terapeutaService.getAllTerapeutas(0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteTerapeuta() {
        int terapeutaId = 1;
        when(terapeutaRepository.existsById(terapeutaId)).thenReturn(true);

        Paciente paciente1 = new Paciente();
        paciente1.setId(10);
        Paciente paciente2 = new Paciente();
        paciente2.setId(20);

        when(pacienteRepository.findByTerapeutaId(terapeutaId))
                .thenReturn(Arrays.asList(paciente1, paciente2));

        terapeutaService.deleteTerapeuta(terapeutaId);

        verify(pacienteService, times(1)).deletePaciente(10);
        verify(pacienteService, times(1)).deletePaciente(20);
        verify(terapeutaRepository, times(1)).deleteById(terapeutaId);
    }

    @Test
    void testDeleteTerapeuta_ThrowsExceptionWhenNotExists() {
        int terapeutaId = 99;
        when(terapeutaRepository.existsById(terapeutaId)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.deleteTerapeuta(terapeutaId));
        verify(pacienteService, never()).deletePaciente(anyInt());
        verify(terapeutaRepository, never()).deleteById(anyInt());
    }

    @Test
    void testUpdateTerapeuta_Success() {
        Integer id = 1;
        TerapeutaInputDTO input = new TerapeutaInputDTO();
        input.setNombre("NuevoNombre");
        input.setApellido("NuevoApellido");
        input.setEmail("nuevo@email.com");
        input.setTelefono("999888777");
        input.setEspecialidad("Neuropsicologia");
        input.setContrasena("nuevaPass");

        Terapeuta terapeuta = new Terapeuta();
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();

        when(terapeutaRepository.findById(id)).thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.encode("nuevaPass")).thenReturn("hashedNuevaPass");
        when(terapeutaRepository.save(terapeuta)).thenReturn(terapeuta);
        when(terapeutaMapper.toDTO(terapeuta)).thenReturn(output);

        TerapeutaOutputDTO result = terapeutaService.updateTerapeuta(id, input);

        assertNotNull(result);
        assertEquals("NuevoNombre", terapeuta.getNombre());
        assertEquals("hashedNuevaPass", terapeuta.getContrasena());
    }

    @Test
    void testUpdateTerapeuta_Partial() {
        Integer id = 1;
        TerapeutaInputDTO input = new TerapeutaInputDTO();
        input.setNombre("SoloNombre");

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setApellido("Existente");
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();

        when(terapeutaRepository.findById(id)).thenReturn(Optional.of(terapeuta));
        when(terapeutaRepository.save(terapeuta)).thenReturn(terapeuta);
        when(terapeutaMapper.toDTO(terapeuta)).thenReturn(output);

        terapeutaService.updateTerapeuta(id, input);

        assertEquals("SoloNombre", terapeuta.getNombre());
        assertEquals("Existente", terapeuta.getApellido());
    }

    @Test
    void testUpdateTerapeuta_NotFound() {
        when(terapeutaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.updateTerapeuta(99, new TerapeutaInputDTO()));
    }

    @Test
    void testGetAgendasByTerapeutaId_Success() {
        Integer id = 1;
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1);
        terapeuta.setNombre("Ana");
        terapeuta.setApellido("Gomez");

        Agenda agenda = new Agenda();
        agenda.setId(10);
        agenda.setNombre("Agenda1");

        AgendaTerapeuta at = new AgendaTerapeuta();
        at.setAgenda(agenda);
        at.setTerapeuta(terapeuta);

        terapeuta.setAgendaTerapeutas(List.of(at));

        PacienteAgenda pa = new PacienteAgenda();
        Paciente paciente = new Paciente();
        paciente.setNombre("Paciente1");
        paciente.setApellido("Apellido1");
        pa.setPaciente(paciente);
        pa.setAgenda(agenda);

        when(terapeutaRepository.findById(id)).thenReturn(Optional.of(terapeuta));
        when(pacienteAgendaRepository.findByAgendaIdAndPacienteId(agenda.getId(), null))
                .thenReturn(pa);

        List<AgendaOutputDTO> result = terapeutaService.getAgendasByTerapeutaId(id);

        assertEquals(1, result.size());
        assertEquals("Agenda1", result.get(0).getNombre());
    }

    @Test
    void testGetAgendasByTerapeutaId_NotFound() {
        when(terapeutaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.getAgendasByTerapeutaId(99));
    }

    @Test
    void testGetSeguimientoByTerapeutaId_Success() {
        Integer terapeutaId = 1;
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1);
        terapeuta.setNombre("Ana");
        terapeuta.setApellido("Gomez");

        Paciente paciente = new Paciente();
        paciente.setId(10);
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");

        Agenda agenda = new Agenda();
        agenda.setId(100);

        PacienteAgenda pa = new PacienteAgenda();
        pa.setAgenda(agenda);

        Juego juego = new Juego();
        juego.setId(1);
        juego.setNombre("Memory");
        juego.setCodigo("MEM01");

        JuegoAgenda ja = new JuegoAgenda();
        ja.setJuego(juego);
        ja.setDificultad(2);
        ja.setRealizado(true);

        when(terapeutaRepository.findById(terapeutaId)).thenReturn(Optional.of(terapeuta));
        when(pacienteRepository.findByTerapeutaId(terapeutaId)).thenReturn(List.of(paciente));
        when(pacienteAgendaRepository.findByPacienteId(paciente.getId()))
                .thenReturn(List.of(pa));
        when(juegoAgendaRepository.findByAgendaId(agenda.getId())).thenReturn(List.of(ja));

        TerapeutaSeguimientoDTO result = terapeutaService.getSeguimientoByTerapeutaId(terapeutaId);

        assertEquals(1, result.getId());
        assertEquals(1, result.getPacientes().size());
        assertEquals(1, result.getPacientes().get(0).getJuegosCompletados());
        assertEquals(1, result.getPacientes().get(0).getJuegosTotales());
    }

    @Test
    void testGetSeguimientoByTerapeutaId_NoPatients() {
        Integer terapeutaId = 1;
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1);
        terapeuta.setNombre("Ana");
        terapeuta.setApellido("Gomez");

        when(terapeutaRepository.findById(terapeutaId)).thenReturn(Optional.of(terapeuta));
        when(pacienteRepository.findByTerapeutaId(terapeutaId)).thenReturn(Collections.emptyList());

        TerapeutaSeguimientoDTO result = terapeutaService.getSeguimientoByTerapeutaId(terapeutaId);

        assertTrue(result.getPacientes().isEmpty());
    }

    @Test
    void testGetSeguimientoByTerapeutaId_NotFound() {
        when(terapeutaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.getSeguimientoByTerapeutaId(99));
    }

    @Test
    void testResetPassword() {
        Integer id = 1;
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNombre("Ana");
        terapeuta.setApellido("Gomez");

        when(terapeutaRepository.findById(id)).thenReturn(Optional.of(terapeuta));
        when(utils.generateRandomPassword("Ana", "Gomez")).thenReturn("newPlainPass");
        when(passwordEncoder.encode("newPlainPass")).thenReturn("newHashedPass");

        PasswordResetDTO result = terapeutaService.resetPassword(id);

        assertEquals("newPlainPass", result.getNuevaContrasenia());
        verify(terapeutaRepository).save(terapeuta);
    }

    @Test
    void testResetPassword_NotFound() {
        when(terapeutaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> terapeutaService.resetPassword(99));
    }
}
