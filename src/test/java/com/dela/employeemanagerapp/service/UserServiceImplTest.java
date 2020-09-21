package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username(USERNAME)
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
        assertEquals(user.getLastLoginDateDisplay(), null);
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
}