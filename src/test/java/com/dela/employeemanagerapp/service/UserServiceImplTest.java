package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String USERNAME = "valid_username";
    private static final String EMAIL = "valid_email@email.com";

    @Mock
    UserRepository userRepository;

    @Mock
    UserFactory userFactory;

    @InjectMocks
    UserServiceImpl userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .roles(Set.of(Role.builder().name(RoleEnum.ROLE_USER).build()))
                .build();
    }

    @Test
    void givenUserExists_newUserPrincipalIsCreated() {
        given(userRepository.findUserByUsername(USERNAME)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        UserDetails userPrincipal = userService.loadUserByUsername(USERNAME);

        then(userRepository).should(times(1)).findUserByUsername(USERNAME);
        then(userRepository).should(times(1)).save(user);

        assertEquals(USERNAME, userPrincipal.getUsername());
        assertNull(user.getLastLoginDateDisplay());
        assertEquals(user.getLastLoginDate(), LocalDate.now());
    }

    @Test
    void givenUserDoesNotExists_exceptionIsThrown() {
        given(userRepository.findUserByUsername(USERNAME)).willReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.loadUserByUsername(USERNAME);
        });

        then(userRepository).should(times(1)).findUserByUsername(USERNAME);
        then(userRepository).should(times(0)).save(user);

        assertThat(exception.getMessage()).isEqualTo("User with username: " + USERNAME + " not found");
    }

    @Nested
    @DisplayName("Reset Password Test")
    class ResetPassword {

        @Test
        void givenUserExists_passwordResets() {
            given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.of(user));
            given(userRepository.save(user)).willReturn(user);
            given(userFactory.resetPassword(user)).willReturn(user);

            userService.resetPassword(user.getEmail());

            then(userFactory).should().resetPassword(user);
            then(userRepository).should().save(user);
        }

        @Test
        void givenUserDoesNotExist_exceptionIsThrown() {
            given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> { userService.resetPassword(user.getEmail()); });

            then(userRepository).should(times(0)).save(user);
        }
    }
}