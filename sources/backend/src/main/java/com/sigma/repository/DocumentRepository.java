package com.sigma.repository;

import com.sigma.model.Document;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;


@Service

public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
  Document findById(Long id);
}
