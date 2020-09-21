package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.repository.AuthorityRepository;
import com.dela.employeemanagerapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
//    fetch all authorities for roles, and return as entities from DB
    public Set<Authority> findAuthoritiesByRoles(Set<Role> userRoles) {
        Set<AuthorityEnum> authorityEnums = userRoles.stream().flatMap(
                userRole ->
                        Arrays.stream(userRole.getName().getAuthorities())
        ).collect(Collectors.toSet());

        return authorityRepository.findAuthoritiesByNameIn(authorityEnums);
    }
}
