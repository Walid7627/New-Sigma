package com.sigma.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sigma.model.Entite;

@Service

public interface EntiteRepository extends PagingAndSortingRepository<Entite, Long>{
	
	
	List<Entite> findByNomSociete(String nomSociete);
	
	Entite findById(long id);
	
	List<Entite> findByAdministrateurId(@Param("administrateur_id") Long administrateurId);
	
	
	List<Entite> findByCodeCPV(@Param("codecpv") String codeCpv);
	  
	List<Entite> findByCodeAPE(@Param("codeape") String codeApe);
	
	List<Entite> findByNumSiret(@Param("num_siret") String numSiret);
}
