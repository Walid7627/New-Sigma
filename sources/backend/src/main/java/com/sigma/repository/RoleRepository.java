package com.sigma.repository;

import com.sigma.model.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;



@Service

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
  Role findById(Long id);

  Role findByName(@Param("name") String name);
}
