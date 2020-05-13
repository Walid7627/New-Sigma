package com.sigma.repository;

import com.sigma.model.Contact;
import com.sigma.model.Fournisseur;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {

    List<Contact> findByNom(@Param("nom") String nom);

    Contact findByMail(@Param("mail") String mail);
    
    Contact findByMailAndFournisseur(@Param("mail") String mail, @Param("fournisseur") Fournisseur fournisseur);

    Optional<Contact> findById(Long id);

    List<Contact> findByFournisseur(@Param("fournisseur") Fournisseur fournisseur);
}
