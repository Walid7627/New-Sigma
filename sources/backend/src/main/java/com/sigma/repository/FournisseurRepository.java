package com.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sigma.model.Fournisseur;
import com.sigma.model.FournisseurType;

@Service

public interface FournisseurRepository extends PagingAndSortingRepository<Fournisseur, Long> {
	List<Fournisseur> findAll();

  List<Fournisseur> findByNom(@Param("nom") String nom);

  Fournisseur findByMail(@Param("mail") String mail);

  Optional<Fournisseur> findById(Long id);

  List<Fournisseur> findByTypeEntreprise(FournisseurType typeEntreprise);

  @Query("select u from Utilisateur u where u.nomSociete like ?1%")
  List<Fournisseur> findByNomSociete(String nom_societe);
  
  List<Fournisseur> findByCodeCPVContaining(String codeCpv);
  List<Fournisseur> findByCodeCPV(String codeCpv);
  
  List<Fournisseur> findByCodeAPE(String codeApe);
  
  List<Fournisseur> findByNumSiret(@Param("num_siret") String numSiret);
  
  List<Fournisseur> findAllByOrderByDateEnregistrementDesc();
}
