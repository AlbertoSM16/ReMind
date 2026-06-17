package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.security.JwtUtil;
import com.remind.back.services.AdministradorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdministradorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdministradorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService administradorService;

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
    public void testCreateAdministrador() throws Exception {
        AdministradorInputDTO input = new AdministradorInputDTO("Juan", "Perez", "juan@mail.com", "pass123", "123456789", "juanadmin");
        AdministradorOutputDTO output = new AdministradorOutputDTO("Juan", "Perez", "juan@mail.com", "123456789");

        when(administradorService.createAdministrador(any(AdministradorInputDTO.class))).thenReturn(output);

        mockMvc.perform(post("/api/administrador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan@mail.com"))
                .andExpect(jsonPath("$.telefono").value("123456789"));
    }
}
