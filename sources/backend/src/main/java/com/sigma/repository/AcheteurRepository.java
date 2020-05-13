package com.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sigma.model.Acheteur;

@Service

public interface AcheteurRepository extends PagingAndSortingRepository<Acheteur, Long> {

    List<Acheteur> findByNom(@Param("nom") String nom);

    Acheteur findByMail(@Param("mail") String mail);

    Optional<Acheteur> findById(Long id);
    
    List<Acheteur> findByEntiteId(@Param("entite_id") long entite_id);
    
    List<Acheteur> findByEquipeId(@Param("equipe_id") Long equipe_id);
}
