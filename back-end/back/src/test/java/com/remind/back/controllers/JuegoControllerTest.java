package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.entities.Juego;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.security.JwtUtil;
import com.remind.back.services.JuegoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JuegoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JuegoService juegoService;

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
    public void testGetAllJuegos() throws Exception {
        Juego juego1 = new Juego(1, "Memoria", "MEM01");
        Juego juego2 = new Juego(2, "Atencion", "ATE01");

        when(juegoService.findAll()).thenReturn(Arrays.asList(juego1, juego2));

        mockMvc.perform(get("/api/juegos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Memoria"))
                .andExpect(jsonPath("$[0].codigo").value("MEM01"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Atencion"))
                .andExpect(jsonPath("$[1].codigo").value("ATE01"));
    }

    @Test
    public void testGetJuegoById_Found() throws Exception {
        Juego juego = new Juego(1, "Memoria", "MEM01");

        when(juegoService.findById(1)).thenReturn(Optional.of(juego));

        mockMvc.perform(get("/api/juegos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Memoria"))
                .andExpect(jsonPath("$.codigo").value("MEM01"));
    }

    @Test
    public void testGetJuegoById_NotFound() throws Exception {
        when(juegoService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/juegos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateJuego() throws Exception {
        Juego input = new Juego(0, "Calculo", "CAL01");
        Juego saved = new Juego(3, "Calculo", "CAL01");

        when(juegoService.save(any(Juego.class))).thenReturn(saved);

        mockMvc.perform(post("/api/juegos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Calculo"))
                .andExpect(jsonPath("$.codigo").value("CAL01"));
    }

    @Test
    public void testDeleteJuego() throws Exception {
        doNothing().when(juegoService).deleteById(1);

        mockMvc.perform(delete("/api/juegos/1"))
                .andExpect(status().isOk());

        verify(juegoService, times(1)).deleteById(1);
    }
}
