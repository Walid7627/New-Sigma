package com.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import com.sigma.model.Visiteur;

@Service

public interface VisiteurRepository extends PagingAndSortingRepository<Visiteur, Long> {
	
	List<Visiteur> findByNom(@Param("nom") String nom);
	
	Visiteur findByMail(@Param("mail") String mail);
	
	Optional<Visiteur> findById(@Param("id") Long id);
	
	
	List<Visiteur> findByEntiteIdIsNull();
}

