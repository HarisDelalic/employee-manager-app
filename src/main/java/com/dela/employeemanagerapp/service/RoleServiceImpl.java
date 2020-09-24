package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.repository.AuthorityRepository;
import com.dela.employeemanagerapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public Role findRoleByName(RoleEnum roleEnum) {
        return roleRepository.findRoleByName(roleEnum);
    }

    @Override
    public Set<Authority> findAuthoritiesByRole(Role role) {
        AuthorityEnum[] authorityEnums = role.getName().getAuthorities();

        return authorityRepository.findAuthoritiesByNameIn(authorityEnums);
    }
}
