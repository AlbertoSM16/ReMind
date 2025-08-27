package com.remind.back.services;

import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;


public interface AdministradorService {

    AdministradorOutputDTO createAdministrador(AdministradorInputDTO administradorInputDTO);

    
}