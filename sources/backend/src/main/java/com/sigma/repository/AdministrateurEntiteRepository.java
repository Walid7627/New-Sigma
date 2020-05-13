package com.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sigma.model.AdministrateurEntite;

@Service

public interface AdministrateurEntiteRepository extends PagingAndSortingRepository<AdministrateurEntite, Long> {
	
	List<AdministrateurEntite> findByNom(@Param("nom") String nom);
	
	AdministrateurEntite findByMail(@Param("mail") String mail);
	
	Optional<AdministrateurEntite> findById(@Param("id") Long id);
	
	
	List<AdministrateurEntite> findByEntiteIdIsNull();
}
