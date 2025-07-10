package com.remind.back.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.entities.Terapeuta;
import com.remind.back.entities.TipoUsuario;
import com.remind.back.Mapper.TerapeutaMapper;
import com.remind.back.repositories.TerapeutaRepository;

@Service
public class TerapeutaServiceImpl implements TerapeutaService {

    @Autowired
    private  TerapeutaRepository terapeutaRepository;

    @Autowired
    private TerapeutaMapper terapeutaMapper;

    @Override
    @Transactional
    public TerapeutaOutputDTO createTerapeuta(TerapeutaInputDTO terapeutaInputDTO){
        Terapeuta terapeuta = terapeutaMapper.TerapeutaInputDTOToTerapeuta(terapeutaInputDTO);

        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeutaRepository.save(terapeuta));
    }

    @Override
    @Transactional(readOnly = true)
    public TerapeutaOutputDTO getTerapeutaById(Integer id){
        Terapeuta terapeuta = terapeutaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con ese id" + id));

        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeuta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TerapeutaOutputDTO>getAllTerapeutas(int page,int size){
        List<Terapeuta> terapeutas = terapeutaRepository.findAll(PageRequest.of(page,size)).getContent();
        return terapeutaRepository.findAll(PageRequest.of(page,size))
            .getContent()
            .stream()
            .map(terapeutaMapper::TerapeutaToTerapeutaOutputDTO)
            .toList();
    }

    @Override
    @Transactional
    public void deleteTerapeuta(Integer id){
        if(!terapeutaRepository.existsById(id)){
            throw new NoSuchElementException("No existe un terapeuta con ese id y no se puede eliminar");
        }
        terapeutaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TerapeutaOutputDTO updateTerapeuta(Integer id, TerapeutaInputDTO terapeutaInputDTO){
        Terapeuta terapeuta = terapeutaRepository.findById(id)
            .orElseThrow(() ->  new NoSuchElementException("El terapeuta con id " + id + " no existe"));
    
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
    
        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeutaRepository.save(terapeuta));
    
    }
}
