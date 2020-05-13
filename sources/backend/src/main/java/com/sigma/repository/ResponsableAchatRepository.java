package com.sigma.repository;

import com.sigma.model.ResponsableAchat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface ResponsableAchatRepository extends PagingAndSortingRepository<ResponsableAchat, Long> {

    List<ResponsableAchat> findByNom(@Param("nom") String nom);

    ResponsableAchat findByMail(@Param("mail") String mail);

    Optional<ResponsableAchat> findById(Long id);
}
