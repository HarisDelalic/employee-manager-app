package com.dela.employeemanagerapp.domain;

import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPrincipalTest {

    @Test
    void getAuthorities() {
//        TODO make some factories for authorities and roles
//        These authorities/roles probably will be used on multiple places
        User user = new User();

        Authority read = Authority.builder()
                .name(AuthorityEnum.USER_READ)
                .build();
        Authority update = Authority.builder()
                .name(AuthorityEnum.USER_UPDATE)
                .build();
        Authority create = Authority.builder()
                .name(AuthorityEnum.USER_CREATE)
                .build();
        Authority delete = Authority.builder()
                .name(AuthorityEnum.USER_DELETE)
                .build();

        Role userRole = Role.builder()
                .name(RoleEnum.ROLE_USER)
                .users(Set.of(user))
                .authorities(Set.of(read))
                .build();

        Role superUserRole = Role.builder()
                .name(RoleEnum.ROLE_SUPERUSER)
                .users(Set.of(user))
                .authorities(Set.of(read, update, create, delete))
                .build();

        user.setRoles(Set.of(userRole, superUserRole));

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Collection<? extends SimpleGrantedAuthority> authorities = Set.of(
                new SimpleGrantedAuthority(AuthorityEnum.USER_READ.getValue()),
                new SimpleGrantedAuthority(AuthorityEnum.USER_UPDATE.getValue()),
                new SimpleGrantedAuthority(AuthorityEnum.USER_CREATE.getValue()),
                new SimpleGrantedAuthority(AuthorityEnum.USER_DELETE.getValue())
        );

        assertEquals(authorities, userPrincipal.getAuthorities());
    }
}