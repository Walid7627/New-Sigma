package com.sigma.service;

import com.sigma.dto.UtilisateurDto;
import com.sigma.utilisateur.Utilisateur;

import java.util.List;

public interface UserService {

    Utilisateur save(UtilisateurDto user);

    List<Utilisateur> findAll();

    void delete(long id);

    Utilisateur findOne(String mail);

    Utilisateur findById(Long id);
}
