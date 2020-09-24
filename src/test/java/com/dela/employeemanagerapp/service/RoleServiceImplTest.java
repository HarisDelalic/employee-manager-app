package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.repository.AuthorityRepository;
import com.dela.employeemanagerapp.repository.RoleRepository;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void findAuthoritiesByRoles() {
        Authority read = Authority.builder()
                .name(AuthorityEnum.USER_READ)
                .build();
        Authority update = Authority.builder()
                .name(AuthorityEnum.USER_UPDATE)
                .build();

        Role managerRole = Role.builder()
                .name(RoleEnum.ROLE_MANAGER)
                .build();

        AuthorityEnum[] authorityEnums = new AuthorityEnum[2];
        authorityEnums[0] = AuthorityEnum.USER_READ;
        authorityEnums[1] = AuthorityEnum.USER_UPDATE;

        when(authorityRepository
                .findAuthoritiesByNameIn(authorityEnums)).thenReturn(Set.of(read, update));

        Collection<Authority> expectedAuthorities = Set.of(read, update);

        assertEquals(expectedAuthorities, roleService.findAuthoritiesByRole(managerRole));
    }
}