package com.remind.back.services;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.remind.back.Mapper.AgendaMapper;
import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.JuegoAsignadoDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.AgendaTerapeuta;
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

import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaMapper agendaMapper;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PacienteAgendaRepository pacienteAgendaRepository;

    @Autowired
    private AgendaTerapeutaRepository agendaTerapeutaRepository;

    @Autowired
    private JuegoAgendaRepository juegoAgendaRepository;

    @Autowired
    private JuegoRepository juegoRepository;

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

    private Integer getPacienteAutenticadoId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return pacienteRepository.findByUsuario(username)
                .orElseThrow(() -> new AccessDeniedException("Paciente no encontrado"))
                .getId();
    }

    private boolean esTerapeuta() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TERAPEUTA"));
    }

    private boolean esPaciente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
    }

    private void verificarAgendaPerteneceATerapeuta(Integer agendaId) {
        if (esAdmin()) return;
        Integer terapeutaId = getTerapeutaAutenticadoId();
        boolean pertenece = agendaTerapeutaRepository.existsByAgendaIdAndTerapeutaId(agendaId, terapeutaId);
        if (!pertenece) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta agenda");
        }
    }

    @Override
    @Transactional
    public AgendaOutputDTO createAgenda(AgendaInputDTO agendaInputDTO) {

        Agenda agenda = agendaMapper.agendaInputDTOToAgenda(agendaInputDTO);
        Agenda savedAgenda = agendaRepository.save(agenda);

        Integer pacienteId = agendaInputDTO.getPaciente_id();

        if (pacienteId != null) {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new NoSuchElementException("Paciente con ID " + pacienteId + " no existe."));
            PacienteAgenda pacienteAgenda = new PacienteAgenda();
            pacienteAgenda.setPaciente(paciente);
            pacienteAgenda.setAgenda(savedAgenda);
            pacienteAgendaRepository.save(pacienteAgenda);
            AgendaTerapeuta agendaTerapeuta = new AgendaTerapeuta();
            agendaTerapeuta.setAgenda(savedAgenda);
            Terapeuta terapeuta = terapeutaRepository.findById(agendaInputDTO.getTerapeuta_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Terapeuta con ID " + agendaInputDTO.getTerapeuta_id() + " no existe."));
            agendaTerapeuta.setTerapeuta(terapeuta);
            agendaTerapeutaRepository.save(agendaTerapeuta);
        }

        return agendaMapper.agendaToAgendaOutputDTO(savedAgenda);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendaOutputDTO getAgendaById(Integer id) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(id);
        }
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agenda con ID " + id + " no existe."));
        return agendaMapper.agendaToAgendaOutputDTO(agenda);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaOutputDTO> getAllAgendas(int page, int size) {
        List<Agenda> agendas = agendaRepository.findAll(PageRequest.of(page, size)).getContent();
        return agendaRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(agendaMapper::agendaToAgendaOutputDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAgenda(Integer id) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(id);
        }
        if (!agendaRepository.existsById(id)) {
            throw new NoSuchElementException("Agenda con ID " + id + " no existe.");
        }

        agendaRepository.deleteById(id);
        pacienteAgendaRepository.deleteByAgendaId(id);
        agendaTerapeutaRepository.deleteByAgendaId(id);
    }

    @Override
    @Transactional
    public AgendaOutputDTO updateAgenda(Integer id, AgendaInputDTO agendaInputDTO) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(id);
        }
        Agenda existingAgenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agenda con ID " + id + " no existe."));

        existingAgenda.setNombre(agendaInputDTO.getNombre());
        Agenda updatedAgenda = agendaRepository.save(existingAgenda);

        if (agendaInputDTO.getPaciente_id() != null) {
            Paciente paciente = pacienteRepository.findById(agendaInputDTO.getPaciente_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Paciente con ID " + agendaInputDTO.getPaciente_id() + " no existe."));

            PacienteAgenda existingPacienteAgenda = pacienteAgendaRepository
                    .findByAgendaIdAndPacienteId(updatedAgenda.getId(), paciente.getId());

        }

        if (agendaInputDTO.getTerapeuta_id() != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(agendaInputDTO.getTerapeuta_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Terapeuta con ID " + agendaInputDTO.getTerapeuta_id() + " no existe."));

            AgendaTerapeuta existingAgendaTerapeuta = agendaTerapeutaRepository
                    .findByAgendaIdAndTerapeutaId(updatedAgenda.getId(), terapeuta.getId());

        }

        return agendaMapper.agendaToAgendaOutputDTO(updatedAgenda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JuegoAsignadoDTO> getJuegosByAgendaId(Integer agendaId) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(agendaId);
        }
        if (!agendaRepository.existsById(agendaId)) {
            throw new NoSuchElementException("Agenda con ID " + agendaId + " no existe.");
        }
        return juegoAgendaRepository.findByAgendaId(agendaId)
                .stream()
                .map(juegoAgenda -> new JuegoAsignadoDTO(
                        juegoAgenda.getJuego().getId(),
                        juegoAgenda.getJuego().getNombre(),
                        juegoAgenda.getJuego().getCodigo(),
                        juegoAgenda.getDificultad(),
                        juegoAgenda.getRealizado()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignJuegoToAgenda(Integer agendaId, Integer juegoId, Integer dificultad) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(agendaId);
        }
        if (juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId).isPresent()) {
            throw new IllegalStateException("Este juego ya ha sido asignado a la agenda.");
        }

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new NoSuchElementException("Agenda con ID " + agendaId + " no existe."));

        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new NoSuchElementException("Juego con ID " + juegoId + " no existe."));

        JuegoAgenda nuevaAsignacion = new JuegoAgenda();
        nuevaAsignacion.setAgenda(agenda);
        nuevaAsignacion.setJuego(juego);
        nuevaAsignacion.setDificultad(dificultad);

        juegoAgendaRepository.save(nuevaAsignacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getJuegosByPacienteId(Integer pacienteId) {
        if (esPaciente()) {
            Integer authPacienteId = getPacienteAutenticadoId();
            if (!authPacienteId.equals(pacienteId)) {
                throw new AccessDeniedException("No tienes permiso para ver los juegos de otro paciente");
            }
        }
        PacienteAgenda pacienteAgenda = pacienteAgendaRepository.findByPacienteId(pacienteId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró agenda para el paciente con ID " + pacienteId));

        Integer agendaId = pacienteAgenda.getAgenda().getId();

        List<JuegoAsignadoDTO> juegos = juegoAgendaRepository.findByAgendaId(agendaId)
                .stream()
                .map(juegoAgenda -> new JuegoAsignadoDTO(
                        juegoAgenda.getJuego().getId(),
                        juegoAgenda.getJuego().getNombre(),
                        juegoAgenda.getJuego().getCodigo(),
                        juegoAgenda.getDificultad(),
                        juegoAgenda.getRealizado()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("agendaId", agendaId);
        response.put("juegos", juegos);

        return response;
    }

    @Override
    @Transactional
    public void completarJuego(Integer agendaId, Integer juegoId) {
        if (esPaciente()) {
            PacienteAgenda pa = pacienteAgendaRepository.findByPacienteId(getPacienteAutenticadoId())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new AccessDeniedException("No tienes una agenda asignada"));
            if (pa.getAgenda().getId() != agendaId) {
                throw new AccessDeniedException("No tienes permiso para completar juegos en esta agenda");
            }
        }
        JuegoAgenda asignacion = juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró la asignación del juego " + juegoId + " en la agenda " + agendaId));

        asignacion.setRealizado(true);
        asignacion.setFechaRealizacion(new Date(System.currentTimeMillis())); 
        juegoAgendaRepository.save(asignacion);
    }

    @Transactional
    public void removeJuegoFromAgenda(Integer agendaId, Integer juegoId) {
        if (esTerapeuta()) {
            verificarAgendaPerteneceATerapeuta(agendaId);
        }
        JuegoAgenda asignacion = juegoAgendaRepository.findByAgendaIdAndJuegoId(agendaId, juegoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró la asignación del juego " + juegoId + " en la agenda " + agendaId));

        juegoAgendaRepository.delete(asignacion);
    }

}
