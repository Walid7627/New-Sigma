package com.sigma.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.sigma.model.AdministrateurSigma;

@Service

public interface AdministrateurSigmaRepository extends PagingAndSortingRepository<AdministrateurSigma, Long>{

}
