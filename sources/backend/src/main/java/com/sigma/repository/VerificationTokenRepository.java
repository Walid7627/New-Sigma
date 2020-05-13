package com.sigma.repository;

import com.sigma.model.VerificationToken;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import com.sigma.utilisateur.Utilisateur;


@Service

public interface VerificationTokenRepository extends PagingAndSortingRepository<VerificationToken, Long> {
  VerificationToken findByUser(Utilisateur user);
  VerificationToken findByToken(String token);
}
