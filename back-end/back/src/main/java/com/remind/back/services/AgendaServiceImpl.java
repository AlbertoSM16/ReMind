package com.remind.back.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.remind.back.Mapper.AgendaMapper;
import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.AgendaTerapeuta;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
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

    @Override
    @Transactional
    public AgendaOutputDTO createAgenda(AgendaInputDTO agendaInputDTO) {

        Agenda agenda = agendaMapper.agendaInputDTOToAgenda(agendaInputDTO);
        Agenda savedAgenda = agendaRepository.save(agenda);

        Integer pacienteId = agendaInputDTO.getPaciente_id();
        
        if(pacienteId != null){
            Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new NoSuchElementException("Paciente con ID " + pacienteId + " no existe."));
            PacienteAgenda pacienteAgenda = new PacienteAgenda();
            pacienteAgenda.setPaciente(paciente);
            pacienteAgenda.setAgenda(savedAgenda);
            pacienteAgendaRepository.save(pacienteAgenda);
            AgendaTerapeuta agendaTerapeuta = new AgendaTerapeuta();
            agendaTerapeuta.setAgenda(savedAgenda);
            Terapeuta terapeuta = terapeutaRepository.findById(agendaInputDTO.getTerapeuta_id())
                .orElseThrow(() -> new NoSuchElementException("Terapeuta con ID " + agendaInputDTO.getTerapeuta_id() + " no existe."));
            agendaTerapeuta.setTerapeuta(terapeuta);
            agendaTerapeutaRepository.save(agendaTerapeuta);
            }

        return agendaMapper.agendaToAgendaOutputDTO(savedAgenda);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendaOutputDTO getAgendaById(Integer id) {

        Agenda agenda = agendaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Agenda con ID " + id + " no existe."));
        return agendaMapper.agendaToAgendaOutputDTO(agenda);
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaOutputDTO> getAllAgendas(int page, int size) {
        List<Agenda> agendas = agendaRepository.findAll(PageRequest.of(page, size)).getContent();
        return agendaRepository.findAll(PageRequest.of(page,size))
            .getContent()
            .stream()
            .map(agendaMapper::agendaToAgendaOutputDTO)
            .toList();
    }
 
    @Override
    @Transactional
    public void deleteAgenda(Integer id) {
        
        if(!agendaRepository.existsById(id)) {
            throw new NoSuchElementException("Agenda con ID " + id + " no existe.");
        }

        agendaRepository.deleteById(id);
        // También eliminar las relaciones en la tabla intermedia
        pacienteAgendaRepository.deleteByAgendaId(id);
        agendaTerapeutaRepository.deleteByAgendaId(id);
    }

    @Override
    @Transactional
    public AgendaOutputDTO updateAgenda(Integer id, AgendaInputDTO agendaInputDTO) {
        Agenda existingAgenda = agendaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Agenda con ID " + id + " no existe."));

        // Actualizar los campos de la agenda existente
        existingAgenda.setNombre(agendaInputDTO.getNombre());

        // Guardar la agenda actualizada
        Agenda updatedAgenda = agendaRepository.save(existingAgenda);

        // Actualizar las relaciones con Paciente y Terapeuta si es necesario
        if (agendaInputDTO.getPaciente_id() != null) {
            Paciente paciente = pacienteRepository.findById(agendaInputDTO.getPaciente_id())
                .orElseThrow(() -> new NoSuchElementException("Paciente con ID " + agendaInputDTO.getPaciente_id() + " no existe."));
            PacienteAgenda pacienteAgenda = new PacienteAgenda();
            pacienteAgenda.setPaciente(paciente);
            pacienteAgenda.setAgenda(updatedAgenda);
            pacienteAgendaRepository.save(pacienteAgenda);
        }

        if (agendaInputDTO.getTerapeuta_id() != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(agendaInputDTO.getTerapeuta_id())
                .orElseThrow(() -> new NoSuchElementException("Terapeuta con ID " + agendaInputDTO.getTerapeuta_id() + " no existe."));
            AgendaTerapeuta agendaTerapeuta = new AgendaTerapeuta();
            agendaTerapeuta.setTerapeuta(terapeuta);
            agendaTerapeuta.setAgenda(updatedAgenda);
            agendaTerapeutaRepository.save(agendaTerapeuta);
        }

        return agendaMapper.agendaToAgendaOutputDTO(updatedAgenda);
    }

    

}
