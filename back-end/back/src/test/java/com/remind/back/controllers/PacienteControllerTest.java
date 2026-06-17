package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.dto.PacienteCreatedDTO;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.security.JwtUtil;
import com.remind.back.services.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AdministradorRepository administradorRepository;

    @MockBean
    private TerapeutaRepository terapeutaRepository;

    @MockBean
    private PacienteRepository pacienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPacientes_Found() throws Exception {
        PacienteOutputDTO output = new PacienteOutputDTO();
        output.setId(1);
        output.setNombre("Paciente1");
        output.setApellido("Apellido1");

        when(pacienteService.getAllPacientes(0, 10)).thenReturn(List.of(output));

        mockMvc.perform(get("/api/paciente?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Paciente1"));
    }

    @Test
    public void testGetAllPacientes_Empty() throws Exception {
        when(pacienteService.getAllPacientes(0, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/paciente?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetPacienteById_Found() throws Exception {
        PacienteOutputDTO output = new PacienteOutputDTO();
        output.setId(1);
        output.setNombre("Paciente1");

        when(pacienteService.getPacienteById(1)).thenReturn(output);

        mockMvc.perform(get("/api/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Paciente1"));
    }

    @Test
    public void testGetPacienteById_NotFound() throws Exception {
        when(pacienteService.getPacienteById(99)).thenReturn(null);

        mockMvc.perform(get("/api/paciente/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Paciente no encontrado"));
    }

    @Test
    public void testCreatePaciente_Success() throws Exception {
        PacienteInputDTO input = new PacienteInputDTO();
        input.setNombre("Paciente1");
        input.setApellido("Apellido1");
        input.setTelefono("123456789");
        input.setEnfermedad("Alzheimer");
        input.setEdad(70);
        input.setNombreResponsable("Responsable1");
        input.setFechaNacimiento(new Date());
        input.setTerapeuta_id(1);

        PacienteCreatedDTO created = new PacienteCreatedDTO("p.apellido", "pass123");

        when(pacienteService.createPaciente(any(PacienteInputDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value("p.apellido"))
                .andExpect(jsonPath("$.contrasenia").value("pass123"));
    }

    private PacienteInputDTO createValidPacienteInput() {
        PacienteInputDTO input = new PacienteInputDTO();
        input.setNombre("Paciente1");
        input.setApellido("Apellido1");
        input.setTelefono("123456789");
        input.setEnfermedad("Alzheimer");
        input.setEdad(70);
        input.setNombreResponsable("Responsable1");
        input.setFechaNacimiento(new Date());
        input.setTerapeuta_id(1);
        return input;
    }

    @Test
    public void testCreatePaciente_Failure() throws Exception {
        PacienteInputDTO input = createValidPacienteInput();
        when(pacienteService.createPaciente(any(PacienteInputDTO.class))).thenThrow(new RuntimeException("Error en base de datos"));

        mockMvc.perform(post("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el paciente: Error en base de datos"));
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        PasswordResetDTO resetDTO = new PasswordResetDTO("newPassword123");

        when(pacienteService.resetPassword(1)).thenReturn(resetDTO);

        mockMvc.perform(post("/api/paciente/1/reset-password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nuevaContrasenia").value("newPassword123"));
    }

    @Test
    public void testResetPassword_NotFound() throws Exception {
        when(pacienteService.resetPassword(99)).thenThrow(new NoSuchElementException("Paciente no encontrado"));

        mockMvc.perform(post("/api/paciente/99/reset-password"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Paciente no encontrado"));
    }

    @Test
    public void testResetPassword_InternalError() throws Exception {
        when(pacienteService.resetPassword(1)).thenThrow(new RuntimeException("Database down"));

        mockMvc.perform(post("/api/paciente/1/reset-password"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al reiniciar la contraseña"));
    }

    @Test
    public void testDeletePaciente_Success() throws Exception {
        doNothing().when(pacienteService).deletePaciente(1);

        mockMvc.perform(delete("/api/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("El paciente ha sido eliminado de la base de datos"));
    }

    @Test
    public void testDeletePaciente_Failure() throws Exception {
        doThrow(new RuntimeException("Could not delete")).when(pacienteService).deletePaciente(1);

        mockMvc.perform(delete("/api/paciente/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puedo eliminar el paciente"));
    }

    @Test
    public void testUpdatePaciente_Success() throws Exception {
        PacienteInputDTO input = createValidPacienteInput();
        input.setNombre("PacienteActualizado");

        PacienteOutputDTO output = new PacienteOutputDTO();
        output.setId(1);
        output.setNombre("PacienteActualizado");

        when(pacienteService.updatePaciente(eq(1), any(PacienteInputDTO.class))).thenReturn(output);

        mockMvc.perform(put("/api/paciente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("PacienteActualizado"));
    }

    @Test
    public void testUpdatePaciente_Failure() throws Exception {
        PacienteInputDTO input = createValidPacienteInput();

        when(pacienteService.updatePaciente(eq(99), any(PacienteInputDTO.class))).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/api/paciente/99" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede editar el paciente 99"));
    }
}
