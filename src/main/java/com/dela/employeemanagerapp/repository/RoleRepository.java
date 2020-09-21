package com.dela.employeemanagerapp.repository;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(RoleEnum roleEnum);

    Set<Role> findRolesByNameIn(Set<RoleEnum> roleEnum);
}
