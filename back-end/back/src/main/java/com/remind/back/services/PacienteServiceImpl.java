package com.remind.back.services;


import java.util.List;
import com.remind.back.entities.Paciente;
import com.remind.back.repositories.PacienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PacienteServiceImpl {

    @Autowired
    private PacienteRepository pacienteRepository;

    
    

}
