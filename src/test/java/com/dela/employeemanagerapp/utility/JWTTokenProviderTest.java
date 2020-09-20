package com.dela.employeemanagerapp.utility;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import io.beanmother.core.ObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JWTTokenProviderTest {
    User user;
    JWTTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        user = new User();

        Authority read = Authority.builder()
                .id(1L)
                .name("authority:read")
                .build();
        Authority write = Authority.builder()
                .id(2L)
                .name("authority:write")
                .build();
        Authority delete = Authority.builder()
                .id(3L)
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

        jwtTokenProvider = new JWTTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "secret");
    }

    @Test
    void generateJwtToken() {
        UserPrincipal userPrincipal = new UserPrincipal(user);

        String jwtToken = jwtTokenProvider.generateJwtToken(userPrincipal);

        System.out.println(jwtToken);
    }

    @Test
    void getClaimsFromJwtToken() {
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                "eyJpc3MiOiJNeSBDb21wYW55IiwiZXhwIjoxNjAwOTkyNDkwLCJpYXQiOjE2MDA1NjA0OTAsImF1dGhvcml0aWVzIjpbImF1dGh" +
                "vcml0eTpkZWxldGUiLCJhdXRob3JpdHk6d3JpdGUiLCJhdXRob3JpdHk6cmVhZCJdfQ" +
                ".7EhW5dPS5I7hgBapilYb_pCH4rqh7JfXIoZFAy5yTVSc-MWWJpxcrfxOVcyHfWvbcUIOjTHs6iLW6nWYMIhG9A\n";

        Set<GrantedAuthority> claimsFromJwtToken = jwtTokenProvider.getClaimsFromJwtToken(jwtToken);

        assertThat(claimsFromJwtToken).contains(new SimpleGrantedAuthority("authority:read"),
                new SimpleGrantedAuthority("authority:write"),
                new SimpleGrantedAuthority("authority:delete"));
    }
}