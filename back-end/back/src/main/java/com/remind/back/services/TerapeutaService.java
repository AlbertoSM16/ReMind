package com.remind.back.services;


import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;

import java.util.List;


public interface TerapeutaService {

    TerapeutaOutputDTO createTerapeuta(TerapeutaInputDTO terapeutaInputDTO);

    TerapeutaOutputDTO getTerapeutaById(Integer id);

    List<TerapeutaOutputDTO> getAllTerapeutas(int page, int size);

    void deleteTerapeuta(Integer id);

    TerapeutaOutputDTO updateTerapeuta(Integer id, TerapeutaInputDTO terapeutaInputDTO);

}
