package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.dto.*;
import com.remind.back.services.TerapeutaService;
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

@WebMvcTest(TerapeutaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TerapeutaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerapeutaService terapeutaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTerapeutas_Found() throws Exception {
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();
        output.setId(1);
        output.setNombre("Terapeuta1");

        when(terapeutaService.getAllTerapeutas(0, 10)).thenReturn(List.of(output));

        mockMvc.perform(get("/api/terapeuta?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Terapeuta1"));
    }

    @Test
    public void testGetAllTerapeutas_Empty() throws Exception {
        when(terapeutaService.getAllTerapeutas(0, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/terapeuta?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetTerapeutaById_Found() throws Exception {
        TerapeutaOutputDTO output = new TerapeutaOutputDTO();
        output.setId(1);
        output.setNombre("Terapeuta1");

        when(terapeutaService.getTerapeutaById(1)).thenReturn(output);

        mockMvc.perform(get("/api/terapeuta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Terapeuta1"));
    }

    @Test
    public void testGetTerapeutaById_NotFound() throws Exception {
        when(terapeutaService.getTerapeutaById(99)).thenReturn(null);

        mockMvc.perform(get("/api/terapeuta/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Paciente no encontrado"));
    }

    @Test
    public void testGetSeguimientoIndividual_Success() throws Exception {
        TerapeutaSeguimientoDTO tracking = new TerapeutaSeguimientoDTO();
        tracking.setId(1);
        tracking.setNombre("Terapeuta1");
        tracking.setPacientes(Collections.emptyList());

        when(terapeutaService.getSeguimientoByTerapeutaId(1)).thenReturn(tracking);

        mockMvc.perform(get("/api/terapeuta/1/seguimiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Terapeuta1"));
    }

    @Test
    public void testGetSeguimientoIndividual_NotFound() throws Exception {
        when(terapeutaService.getSeguimientoByTerapeutaId(99)).thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(get("/api/terapeuta/99/seguimiento"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTerapeuta_Success() throws Exception {
        TerapeutaInputDTO input = new TerapeutaInputDTO();
        input.setNombre("Terapeuta1");
        input.setApellido("Apellido1");
        input.setEmail("t@mail.com");
        input.setTelefono("123456789");
        input.setEspecialidad("Neuro");
        input.setFechaNacimiento(new Date());

        TerapeutaCreatedDTO created = new TerapeutaCreatedDTO("t.usuario", "pass123");

        when(terapeutaService.createTerapeuta(any(TerapeutaInputDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/terapeuta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value("t.usuario"))
                .andExpect(jsonPath("$.contrasena").value("pass123"));
    }

    @Test
    public void testCreateTerapeuta_Failure() throws Exception {
        TerapeutaInputDTO input = new TerapeutaInputDTO();

        when(terapeutaService.createTerapeuta(any(TerapeutaInputDTO.class))).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(post("/api/terapeuta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el terapeuta: DB Error"));
    }

    @Test
    public void testDeleteTerapeuta_Success() throws Exception {
        doNothing().when(terapeutaService).deleteTerapeuta(1);

        mockMvc.perform(delete("/api/terapeuta/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTerapeuta_Failure() throws Exception {
        doThrow(new RuntimeException("Could not delete")).when(terapeutaService).deleteTerapeuta(1);

        mockMvc.perform(delete("/api/terapeuta/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede eliminar el terapeuta con id: 1"));
    }

    @Test
    public void testUpdateTerapeuta() throws Exception {
        TerapeutaInputDTO input = new TerapeutaInputDTO();
        input.setNombre("TerapeutaActualizado");

        TerapeutaOutputDTO output = new TerapeutaOutputDTO();
        output.setId(1);
        output.setNombre("TerapeutaActualizado");

        when(terapeutaService.updateTerapeuta(eq(1), any(TerapeutaInputDTO.class))).thenReturn(output);

        mockMvc.perform(put("/api/terapeuta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("TerapeutaActualizado"));
    }

    @Test
    public void testGetAgendasDelTerapeuta_Found() throws Exception {
        AgendaOutputDTO agenda = new AgendaOutputDTO(1, "Agenda1", "Paciente1", "Terapeuta1");

        when(terapeutaService.getAgendasByTerapeutaId(1)).thenReturn(List.of(agenda));

        mockMvc.perform(get("/api/terapeuta/1/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Agenda1"));
    }

    @Test
    public void testGetAgendasDelTerapeuta_Empty() throws Exception {
        when(terapeutaService.getAgendasByTerapeutaId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/terapeuta/1/agendas"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron agendas para este terapeuta."));
    }

    @Test
    public void testGetAgendasDelTerapeuta_Failure() throws Exception {
        when(terapeutaService.getAgendasByTerapeutaId(1)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/terapeuta/1/agendas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al recuperar las agendas."));
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        PasswordResetDTO resetDTO = new PasswordResetDTO("newPass");

        when(terapeutaService.resetPassword(1)).thenReturn(resetDTO);

        mockMvc.perform(post("/api/terapeuta/1/reset-password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nuevaContrasenia").value("newPass"));
    }

    @Test
    public void testResetPassword_NotFound() throws Exception {
        when(terapeutaService.resetPassword(99)).thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(post("/api/terapeuta/99/reset-password"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void testResetPassword_InternalError() throws Exception {
        when(terapeutaService.resetPassword(1)).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(post("/api/terapeuta/1/reset-password"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al reiniciar la contraseña"));
    }
}
