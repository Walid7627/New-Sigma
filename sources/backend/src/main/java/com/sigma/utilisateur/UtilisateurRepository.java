package com.sigma.utilisateur;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// @Service(value = "utilisateurRepository")
public interface UtilisateurRepository extends PagingAndSortingRepository<Utilisateur, Long> {

    List<Utilisateur> findByNom(@Param("nom") String nom);

    Utilisateur findByMail(@Param("mail") String mail);

    Optional<Utilisateur> findById(Long id);
}
