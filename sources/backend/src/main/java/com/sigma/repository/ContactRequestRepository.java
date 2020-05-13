package com.sigma.repository;


import com.sigma.model.ContactRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ContactRequestRepository extends PagingAndSortingRepository<ContactRequest, Long> {

	List<ContactRequest> findByNom(@Param("nom") String nom);
	ContactRequest findByMail(@Param("mail") String mail);
	Optional<ContactRequest> findById(Long id);
}
