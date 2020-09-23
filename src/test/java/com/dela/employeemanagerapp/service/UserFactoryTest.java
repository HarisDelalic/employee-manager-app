package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserFactoryTest {
    private static final String USERNAME = "valid_username";
    private static final String EMAIL = "valid_email@email.com";
    private static final String OLD_ENCODED_PASSWORD = "old_encoded_password";
    private static final String NEW_PLAIN_PASSWORD = "new_plain_password";
    private static final String NEW_ENCODED_PASSWORD = "new_encoded_password";

    @Mock
    PasswordFactory passwordFactory;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    EmailSender emailSender;
    @InjectMocks
    UserFactory userFactory;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(OLD_ENCODED_PASSWORD)
                .roles(Set.of(Role.builder().name(RoleEnum.ROLE_USER).build()))
                .build();
    }

    @Test
    void resetPassword() {
        given(passwordFactory.generatePassword()).willReturn(NEW_PLAIN_PASSWORD);
        given(passwordEncoder.encode(NEW_PLAIN_PASSWORD)).willReturn(NEW_ENCODED_PASSWORD);

        User withNewPassword = userFactory.resetPassword(this.user);

        assertEquals(NEW_ENCODED_PASSWORD, user.getPassword());
        assertEquals(NEW_ENCODED_PASSWORD, withNewPassword.getPassword());

        then(emailSender).should().createAndSendNewUserEmail(user.getEmail(), NEW_PLAIN_PASSWORD);
    }
}