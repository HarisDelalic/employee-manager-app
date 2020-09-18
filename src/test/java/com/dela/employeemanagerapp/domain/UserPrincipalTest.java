package com.dela.employeemanagerapp.domain;

import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class UserPrincipalTest {

    @Test
    void getAuthorities() {
//        TODO make some factories for authorities and roles
//        These authorities/roles probably will be used on multiple places
        User user = new User();

        Authority read = Authority.builder()
                .id(1L)
                .name("authority:read")
                .build();
        Authority write = Authority.builder()
                .id(2L)
                .name("authority:write")
                .build();
        Authority delete = Authority.builder()
                .id(2L)
                .name("authority:delete")
                .build();

        Role userRole = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .users(Set.of(user))
                .authorities(Set.of(read, write))
                .build();

        Role adminRole = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .users(Set.of(user))
                .authorities(Set.of(read, write, delete))
                .build();

        user.setRoles(Set.of(userRole, adminRole));
        read.setRoles(Set.of(userRole));
        write.setRoles(Set.of(userRole));
        delete.setRoles(Set.of(userRole));


        UserPrincipal userPrincipal = new UserPrincipal(user);

        Collection<? extends SimpleGrantedAuthority> authorities = Set.of(
                new SimpleGrantedAuthority(write.getName()),
                new SimpleGrantedAuthority(read.getName()),
                new SimpleGrantedAuthority(delete.getName())
        );

        assertEquals(userPrincipal.getAuthorities(), authorities);
    }
}