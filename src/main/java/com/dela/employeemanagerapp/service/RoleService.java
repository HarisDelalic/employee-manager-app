package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;

import java.util.Set;

public interface RoleService {
    Role findRoleByName(RoleEnum roleEnum);

    Set<Role> findRolesByNameIn(Set<RoleEnum> roleEnum);

    Set<Authority> findAuthoritiesByRole(Role role);
}
