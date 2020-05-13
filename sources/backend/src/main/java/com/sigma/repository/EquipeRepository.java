package com.sigma.repository;

import com.sigma.model.Equipe;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service

public interface EquipeRepository extends PagingAndSortingRepository<Equipe, Long> {
  Equipe findById(Long id);
  
  Equipe findOneByEntiteIdAndLibelleIgnoreCase(long entite_id, String libelle);

  List<Equipe> findByEntiteId(Long id);
  
  List<Equipe> findByLibelle(String libelle);
}
