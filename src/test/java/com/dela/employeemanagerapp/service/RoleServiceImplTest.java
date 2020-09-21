package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.repository.AuthorityRepository;
import com.dela.employeemanagerapp.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @Mock
    AuthorityRepository authorityRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    @Test
    void findAuthoritiesByRoles() {
        User user = new User();

        Authority read = Authority.builder()
                .name(AuthorityEnum.USER_READ)
                .build();
        Authority update = Authority.builder()
                .name(AuthorityEnum.USER_UPDATE)
                .build();

        Role userRole = Role.builder()
                .name(RoleEnum.ROLE_USER)
                .build();

        Role managerRole = Role.builder()
                .name(RoleEnum.ROLE_MANAGER)
                .build();

        user.setRoles(Set.of(userRole, managerRole));

        when(authorityRepository
                .findAuthoritiesByNameIn(Set.of(AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE)))
                .thenReturn(Set.of(read, update));

        roleService.findAuthoritiesByRoles(user.getRoles());

        Collection<Authority> expectedAuthorities = Set.of(read, update);

        assertEquals(expectedAuthorities, roleService.findAuthoritiesByRoles(user.getRoles()));
    }
}