package com.remind.back.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.JuegoAsignadoDTO;
import com.remind.back.dto.PacienteSeguimientoDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.dto.TerapeutaSeguimientoDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.JuegoAgenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.Mapper.TerapeutaMapper;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.PacienteTerapeutaRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.utils.Utils;

import org.springframework.security.access.AccessDeniedException;

@Service
public class TerapeutaServiceImpl implements TerapeutaService {

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PacienteTerapeutaRepository pacienteTerapeutaRepository;

    @Autowired
    private JuegoAgendaRepository juegoAgendaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteAgendaRepository pacienteAgendaRepository;

    @Autowired
    private TerapeutaMapper terapeutaMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Utils utils;

    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    private Integer getTerapeutaAutenticadoId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return terapeutaRepository.findByUsuario(username)
                .orElseThrow(() -> new AccessDeniedException("Terapeuta no encontrado"))
                .getId();
    }

    private void verificarPropietarioTerapeuta(Integer id) {
        if (!esAdmin()) {
            Integer authId = getTerapeutaAutenticadoId();
            if (!authId.equals(id)) {
                throw new AccessDeniedException("No tienes permiso para acceder a este terapeuta");
            }
        }
    }

    @Override
    @Transactional
    public TerapeutaCreatedDTO createTerapeuta(TerapeutaInputDTO terapeutaInputDTO) {


        String passwordPlano = utils.generateRandomPassword(terapeutaInputDTO.getNombre(),
                terapeutaInputDTO.getApellido());
        String hashedPassword = passwordEncoder.encode(passwordPlano);
        terapeutaInputDTO.setContrasena(hashedPassword);

        String usuario = utils.generateRandomUsername(terapeutaInputDTO.getNombre(), terapeutaInputDTO.getApellido());
        terapeutaInputDTO.setUsuario(usuario);
        Terapeuta terapeuta = terapeutaMapper.toTerapeuta(terapeutaInputDTO);
        terapeutaRepository.save(terapeuta);

        return new TerapeutaCreatedDTO(usuario, passwordPlano);
    }

    @Override
    @Transactional(readOnly = true)
    public TerapeutaOutputDTO getTerapeutaById(Integer id) {
        verificarPropietarioTerapeuta(id);
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con ese id" + id));

        return terapeutaMapper.toDTO(terapeuta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TerapeutaOutputDTO> getAllTerapeutas(int page, int size) {
        List<Terapeuta> terapeutas = terapeutaRepository.findAll(PageRequest.of(page, size)).getContent();
        return terapeutaRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(terapeutaMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteTerapeuta(int id) {
        verificarPropietarioTerapeuta(id);
        if (!terapeutaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe un terapeuta con ese id y no se puede eliminar");
        }

        List<Paciente> pacientesAsociados = pacienteRepository.findByTerapeutaId(id);

        for (Paciente paciente : pacientesAsociados) {
            pacienteService.deletePaciente(paciente.getId());
        }

        terapeutaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TerapeutaOutputDTO updateTerapeuta(Integer id, TerapeutaInputDTO terapeutaInputDTO) {
        verificarPropietarioTerapeuta(id);
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El terapeuta con id " + id + " no existe"));

        if (terapeutaInputDTO.getNombre() != null) {
            terapeuta.setNombre(terapeutaInputDTO.getNombre());
        }
        if (terapeutaInputDTO.getApellido() != null) {
            terapeuta.setApellido(terapeutaInputDTO.getApellido());
        }
        if (terapeutaInputDTO.getEmail() != null) {
            terapeuta.setEmail(terapeutaInputDTO.getEmail());
        }
        if (terapeutaInputDTO.getTelefono() != null) {
            terapeuta.setTelefono(terapeutaInputDTO.getTelefono());
        }
        if (terapeutaInputDTO.getEspecialidad() != null) {
            terapeuta.setEspecialidad(terapeutaInputDTO.getEspecialidad());
        }
        if (terapeutaInputDTO.getContrasena() != null) {
            String hashedPassword = passwordEncoder.encode(terapeutaInputDTO.getContrasena());
            terapeuta.setContrasena(hashedPassword);
        }

        return terapeutaMapper.toDTO(terapeutaRepository.save(terapeuta));

    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaOutputDTO> getAgendasByTerapeutaId(Integer id) {
        verificarPropietarioTerapeuta(id);
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con el id " + id));

        List<Agenda> agendas = terapeuta.getAgendaTerapeutas()
                .stream()
                .map(agendaConexion -> agendaConexion.getAgenda())
                .collect(Collectors.toList());

        return agendas.stream().map(agenda -> {
            PacienteAgenda pa = pacienteAgendaRepository.findByAgendaIdAndPacienteId(agenda.getId(), null);

            String nombrePaciente = "Paciente no asignado";
            if (pa != null && pa.getPaciente() != null) {
                nombrePaciente = pa.getPaciente().getNombre() + " " + pa.getPaciente().getApellido();
            }

            return new AgendaOutputDTO(
                    agenda.getId(),
                    agenda.getNombre(),
                    nombrePaciente,
                    terapeuta.getNombre() + " " + terapeuta.getApellido());
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TerapeutaSeguimientoDTO getSeguimientoByTerapeutaId(Integer terapeutaId) {
        verificarPropietarioTerapeuta(terapeutaId);
        Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con el id " + terapeutaId));

        List<Paciente> pacientes = pacienteRepository.findByTerapeutaId(terapeutaId);

        List<PacienteSeguimientoDTO> pacientesDelTerapeuta = pacientes
                .stream()
                .map(paciente -> {
                    Integer agendaId = pacienteAgendaRepository.findByPacienteId(paciente.getId())
                            .stream()
                            .map(pacienteAgenda -> pacienteAgenda.getAgenda().getId())
                            .findFirst()
                            .orElse(null);

                    long juegosCompletados = 0;
                    long juegosTotales = 0;

                    List<JuegoAsignadoDTO> juegosAsignadosDto = new java.util.ArrayList<>();

                    if (agendaId != null) {
                        List<JuegoAgenda> juegosAsignados = juegoAgendaRepository.findByAgendaId(agendaId);
                        juegosTotales = juegosAsignados.size();
                        juegosCompletados = juegosAsignados.stream()
                                .filter(juego -> juego.getRealizado() != null && juego.getRealizado())
                                .count();

                        juegosAsignadosDto = juegosAsignados.stream()
                                .map(juegoAgenda -> new JuegoAsignadoDTO(
                                        juegoAgenda.getJuego().getId(),
                                        juegoAgenda.getJuego().getNombre(),
                                        juegoAgenda.getJuego().getCodigo(),
                                        juegoAgenda.getDificultad(),
                                        juegoAgenda.getRealizado() != null && juegoAgenda.getRealizado()))
                                .collect(Collectors.toList());
                    }

                    return new PacienteSeguimientoDTO(
                            paciente.getId(),
                            paciente.getNombre() + " " + paciente.getApellido(),
                            juegosCompletados,
                            juegosTotales,
                            juegosAsignadosDto);
                }).collect(Collectors.toList());

        return new TerapeutaSeguimientoDTO(
                terapeuta.getId(),
                terapeuta.getNombre() + " " + terapeuta.getApellido(),
                pacientesDelTerapeuta);
    }

    @Override
    @Transactional
    public PasswordResetDTO resetPassword(Integer id) {
        verificarPropietarioTerapeuta(id);
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El paciente con id " + id + " no existe"));

        String newPasswordPlano = utils.generateRandomPassword(terapeuta.getNombre(), terapeuta.getApellido());
        String hashedPassword = passwordEncoder.encode(newPasswordPlano);

        terapeuta.setContrasena(hashedPassword);
        terapeutaRepository.save(terapeuta);

        return new PasswordResetDTO(newPasswordPlano);
    }

}
