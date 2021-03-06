package com.dela.employeemanagerapp.utility;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JWTTokenProviderTest {
    User user;
    JWTTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        user = new User();

        Authority read = Authority.builder()
                .id(1L)
                .name(AuthorityEnum.USER_READ)
                .build();
        Authority update = Authority.builder()
                .id(2L)
                .name(AuthorityEnum.USER_UPDATE)
                .build();
        Authority delete = Authority.builder()
                .id(2L)
                .name(AuthorityEnum.USER_DELETE)
                .build();

        Role userRole = Role.builder()
                .id(1L)
                .name(RoleEnum.ROLE_USER)
                .authorities(Set.of(read))
                .build();

        Role superUserRole = Role.builder()
                .id(1L)
                .name(RoleEnum.ROLE_SUPERUSER)
                .authorities(Set.of(read, update, delete))
                .build();

        user.setRoles(Set.of(userRole, superUserRole));

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

        Set<GrantedAuthority> claimsFromJwtToken = jwtTokenProvider.getAuthoritiesFromJwtToken(jwtToken);

        assertThat(claimsFromJwtToken).contains(new SimpleGrantedAuthority("authority:read"),
                new SimpleGrantedAuthority("authority:write"),
                new SimpleGrantedAuthority("authority:delete"));
    }

    @Nested
    @DisplayName("Tests for isJwtTokenValid")
    class TokenValidatorTest {
        String notExpiredJwtToken;
        String expiredJwtToken;

        @BeforeEach
        void setUp() {
            notExpiredJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                    "eyJpc3MiOiJNeSBDb21wYW55IiwiZXhwIjoxNjAwOTkyNDkwLCJpYXQiOjE2MDA1NjA0OTAsImF1dGhvcml0aWVzIjpbImF1dGh" +
                    "vcml0eTpkZWxldGUiLCJhdXRob3JpdHk6d3JpdGUiLCJhdXRob3JpdHk6cmVhZCJdfQ" +
                    ".7EhW5dPS5I7hgBapilYb_pCH4rqh7JfXIoZFAy5yTVSc-MWWJpxcrfxOVcyHfWvbcUIOjTHs6iLW6nWYMIhG9A\n";

            expiredJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                    "eyJpc3MiOiJNeSBDb21wYW55IiwiZXhwIjoxNjAwMTgyMDcxLCJpYXQiOjE2MDA2MTQwNzEsImF1dGhvcml0aWVzIjpbImF1dGh" +
                    "vcml0eTpkZWxldGUiLCJhdXRob3JpdHk6d3JpdGUiLCJhdXRob3JpdHk6cmVhZCJdfQ" +
                    ".j7nYSvOzc2P38E4TFFUOMkLvmbuvFgGW35i5x_qoHqjiZiPsN0PozfmQrA5LqeKCjG1gCUyPUZvi18_wkhZzIQ\n";
        }

        @Test
        void notValidSinceUsernameIsNull() {
            user.setUsername(null);

            assertThat(jwtTokenProvider.isJwtTokenValid(user.getUsername(), notExpiredJwtToken)).isFalse();
        }

        @Test
        void notValidSinceUsernameIsEmptyString() {
            user.setUsername("");

            assertThat(jwtTokenProvider.isJwtTokenValid(user.getUsername(), notExpiredJwtToken)).isFalse();
        }

        @Test()
        void notValidSinceTokenIsExpired() {
            user.setUsername("valid_username");

            assertThat(jwtTokenProvider.isJwtTokenValid(user.getUsername(), expiredJwtToken)).isFalse();
        }

        @Test()
        void validSinceUsernameCorrectAndNotExpiredToken() {
            user.setUsername("valid_username");

            assertThat(jwtTokenProvider.isJwtTokenValid(user.getUsername(), notExpiredJwtToken)).isTrue();
        }
    }
}