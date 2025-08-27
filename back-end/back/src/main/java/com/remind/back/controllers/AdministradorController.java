package com.remind.back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.services.AdministradorService;

@RestController
@RequestMapping("/api/administrador")
@CrossOrigin(origins = "*")

public class AdministradorController {
    
    @Autowired
    private AdministradorService administradorService;
    
    @PostMapping()
    public ResponseEntity<AdministradorOutputDTO> createAdministrador(@RequestBody AdministradorInputDTO administradorInputDTO) {
        return new ResponseEntity<>(administradorService.createAdministrador(administradorInputDTO), HttpStatus.CREATED);
    }

}
