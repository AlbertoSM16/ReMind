package com.remind.back.services;

import com.remind.back.entities.Usuario;

public interface UserService {

    Usuario findById(Integer id);

    Usuario findByEmail(String email);

    Usuario save(Usuario usuario);

    void deleteById(Integer id);

    boolean existsByEmail(String email);
    
}
