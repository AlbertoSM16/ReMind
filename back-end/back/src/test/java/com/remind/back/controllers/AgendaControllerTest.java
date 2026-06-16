package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.dto.*;
import com.remind.back.services.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllAgendas_Found() throws Exception {
        AgendaOutputDTO output = new AgendaOutputDTO(1, "Agenda1", "Paciente1", "Terapeuta1");

        when(agendaService.getAllAgendas(0, 10)).thenReturn(List.of(output));

        mockMvc.perform(get("/api/agenda?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Agenda1"));
    }

    @Test
    public void testGetAllAgendas_Empty() throws Exception {
        when(agendaService.getAllAgendas(0, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/agenda?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetAgendaById_Found() throws Exception {
        AgendaOutputDTO output = new AgendaOutputDTO(1, "Agenda1", "Paciente1", "Terapeuta1");

        when(agendaService.getAgendaById(1)).thenReturn(output);

        mockMvc.perform(get("/api/agenda/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Agenda1"));
    }

    @Test
    public void testGetAgendaById_NotFound() throws Exception {
        when(agendaService.getAgendaById(99)).thenReturn(null);

        mockMvc.perform(get("/api/agenda/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Agenda no encontrada"));
    }

    @Test
    public void testGetJuegosDelPaciente_Success() throws Exception {
        Map<String, Object> mockResponse = Map.of("agendaId", 1, "juegos", Collections.emptyList());

        when(agendaService.getJuegosByPacienteId(1)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/agenda/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId").value(1));
    }

    @Test
    public void testGetJuegosDelPaciente_NotFound() throws Exception {
        when(agendaService.getJuegosByPacienteId(99)).thenThrow(new NoSuchElementException("Paciente no encontrado"));

        mockMvc.perform(get("/api/agenda/paciente/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Paciente no encontrado"));
    }

    @Test
    public void testCompletarJuego_Success() throws Exception {
        doNothing().when(agendaService).completarJuego(1, 2);

        mockMvc.perform(put("/api/agenda/1/juego/2/completar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Juego marcado como completado."));
    }

    @Test
    public void testCompletarJuego_NotFound() throws Exception {
        doThrow(new NoSuchElementException("Agenda o Juego no encontrado")).when(agendaService).completarJuego(1, 99);

        mockMvc.perform(put("/api/agenda/1/juego/99/completar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Agenda o Juego no encontrado"));
    }

    @Test
    public void testCreateAgenda_Success() throws Exception {
        AgendaInputDTO input = new AgendaInputDTO("Nueva Agenda", 1, 2);
        AgendaOutputDTO output = new AgendaOutputDTO(5, "Nueva Agenda", "Paciente1", "Terapeuta1");

        when(agendaService.createAgenda(any(AgendaInputDTO.class))).thenReturn(output);

        mockMvc.perform(post("/api/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Nueva Agenda"));
    }

    @Test
    public void testCreateAgenda_Failure() throws Exception {
        AgendaInputDTO input = new AgendaInputDTO("Nueva Agenda", 1, 2);

        when(agendaService.createAgenda(any(AgendaInputDTO.class))).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(post("/api/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al crear la agenda"));
    }

    @Test
    public void testUpdateAgenda_Success() throws Exception {
        AgendaInputDTO input = new AgendaInputDTO("Agenda Actualizada", 1, 2);
        AgendaOutputDTO output = new AgendaOutputDTO(1, "Agenda Actualizada", "Paciente1", "Terapeuta1");

        when(agendaService.updateAgenda(eq(1), any(AgendaInputDTO.class))).thenReturn(output);

        mockMvc.perform(put("/api/agenda/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Agenda Actualizada"));
    }

    @Test
    public void testUpdateAgenda_NotFound() throws Exception {
        AgendaInputDTO input = new AgendaInputDTO("Agenda Actualizada", 1, 2);

        when(agendaService.updateAgenda(eq(99), any(AgendaInputDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/agenda/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Agenda no encontrada"));
    }

    @Test
    public void testDeleteAgenda_Success() throws Exception {
        doNothing().when(agendaService).deleteAgenda(1);

        mockMvc.perform(delete("/api/agenda/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAgenda_Failure() throws Exception {
        doThrow(new RuntimeException("Error")).when(agendaService).deleteAgenda(99);

        mockMvc.perform(delete("/api/agenda/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al eliminar la agenda"));
    }

    @Test
    public void testAssignJuegoToAgenda_Success() throws Exception {
        AssignJuegoDTO assignDTO = new AssignJuegoDTO();
        assignDTO.setJuegoId(3);
        assignDTO.setDificultad(2);

        doNothing().when(agendaService).assignJuegoToAgenda(1, 3, 2);

        mockMvc.perform(post("/api/agenda/1/juego")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Juego asignado correctamente a la agenda."));
    }

    @Test
    public void testAssignJuegoToAgenda_NotFound() throws Exception {
        AssignJuegoDTO assignDTO = new AssignJuegoDTO();
        assignDTO.setJuegoId(99);
        assignDTO.setDificultad(2);

        doThrow(new NoSuchElementException("Juego no encontrado")).when(agendaService).assignJuegoToAgenda(1, 99, 2);

        mockMvc.perform(post("/api/agenda/1/juego")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Juego no encontrado"));
    }

    @Test
    public void testAssignJuegoToAgenda_Conflict() throws Exception {
        AssignJuegoDTO assignDTO = new AssignJuegoDTO();
        assignDTO.setJuegoId(3);
        assignDTO.setDificultad(2);

        doThrow(new IllegalStateException("Juego ya asignado")).when(agendaService).assignJuegoToAgenda(1, 3, 2);

        mockMvc.perform(post("/api/agenda/1/juego")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Juego ya asignado"));
    }

    @Test
    public void testGetJuegosDeLaAgenda_Success() throws Exception {
        JuegoAsignadoDTO juego = new JuegoAsignadoDTO(3, "Memory", "MEM01", 2, false);

        when(agendaService.getJuegosByAgendaId(1)).thenReturn(List.of(juego));

        mockMvc.perform(get("/api/agenda/1/juegos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].nombre").value("Memory"))
                .andExpect(jsonPath("$[0].realizado").value(false));
    }

    @Test
    public void testGetJuegosDeLaAgenda_NotFound() throws Exception {
        when(agendaService.getJuegosByAgendaId(99)).thenThrow(new NoSuchElementException("Agenda no encontrada"));

        mockMvc.perform(get("/api/agenda/99/juegos"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Agenda no encontrada"));
    }

    @Test
    public void testRemoveJuegoDeAgenda_Success() throws Exception {
        doNothing().when(agendaService).removeJuegoFromAgenda(1, 3);

        mockMvc.perform(delete("/api/agenda/1/juego/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Juego eliminado de la agenda correctamente."));
    }

    @Test
    public void testRemoveJuegoDeAgenda_NotFound() throws Exception {
        doThrow(new NoSuchElementException("Asociacion no encontrada")).when(agendaService).removeJuegoFromAgenda(1, 99);

        mockMvc.perform(delete("/api/agenda/1/juego/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Asociacion no encontrada"));
    }
}
