package com.dela.employeemanagerapp.repository;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Set<Authority> findAuthoritiesByNameIn(AuthorityEnum[] authorityEnums);
}
