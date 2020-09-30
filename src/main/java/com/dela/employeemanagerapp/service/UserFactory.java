package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFactory {

    private final PasswordFactory passwordFactory;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final UserRepository userRepository;

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

    public User updateUser(String oldUsername, User userData, Set<Role> userRoles) {
        User user = userRepository.findUserByUsername(oldUsername).map(foundUser -> {
            foundUser.setFirstName(userData.getFirstName());
            foundUser.setLastName(userData.getLastName());
            foundUser.setUsername(userData.getUsername());
            foundUser.setEmail(userData.getEmail());
            foundUser.setActive(userData.isActive());
            foundUser.setLocked(userData.isLocked());
            foundUser.setRoles(userRoles);
            return foundUser;
        }).orElseThrow(() -> new UserNotFoundException("User not Found"));
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
