package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFactory {

    private final PasswordFactory passwordFactory;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    public User createNewUser(User userData, Set<Role> userRoles) {
        User user = User.builder()
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .username(userData.getUsername())
                .email(userData.getEmail())
                .profileImageUrl(userData.getProfileImageUrl())
                .joinDate(LocalDate.now())
                .roles(userRoles)
                .isActive(true)
                .isLocked(false)
                .build();

        createPasswordAndNotifyUser(user);

        return user;
    }

    public User updateUser(User userData, Set<Role> userRoles) {
        User user = User.builder()
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .username(userData.getUsername())
                .email(userData.getEmail())
                .roles(userRoles)
                .isActive(userData.isActive())
                .isLocked(userData.isLocked())
                .build();
        return user;
    }

    public User resetPassword(User user) {
        return createPasswordAndNotifyUser(user);
    }

    private User createPasswordAndNotifyUser(User user) {
        String plainPassword = passwordFactory.generatePassword();
        String encodedPassword = passwordEncoder.encode(plainPassword);

        log.info(plainPassword);

        emailSender.createAndSendNewUserEmail(user.getEmail(), plainPassword);

        user.setPassword(encodedPassword);

        return user;
    }
}
