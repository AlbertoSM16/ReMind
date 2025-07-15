package com.remind.back.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remind.back.Mapper.AgendaMapper;
import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.AgendaTerapeuta;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;

@Service
public class AgendaServiceImpl implements AgendaService {


    @Autowired
    private AgendaRepository agendaRepository; 

    @Autowired
    private AgendaMapper agendaMapper;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private PacienteAgendaRepository pacienteAgendaRepository;

    @Autowired
    private AgendaTerapeutaRepository agendaTerapeutaRepository;

    @Override
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
            agendaTerapeuta.setTerapeuta(paciente.getTerapeuta());
            agendaTerapeutaRepository.save(agendaTerapeuta);
            }

        return agendaMapper.agendaToAgendaOutputDTO(savedAgenda);
    }

    @Override
    public AgendaOutputDTO getAgendaById(Integer id) {
        // Implementation here
        return null;
    }

    @Override
    public List<AgendaOutputDTO> getAllAgendas(int page, int size) {
        // Implementation here
        return null;
    }
 
    @Override
    public void deleteAgenda(Integer id) {
        // Implementation here
    }

    @Override
    public AgendaOutputDTO updateAgenda(Integer id, AgendaInputDTO agendaInputDTO) {
        // Implementation here
        return null;
    }

    

}
