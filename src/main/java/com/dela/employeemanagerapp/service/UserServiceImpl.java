package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.config.SecurityConfig;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import com.dela.employeemanagerapp.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
@Qualifier("userServiceImpl")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    //    @Override
    public UserDetails loadUserByUsername(String username) {
        User foundUser = userRepository.findUserByUsername(username).map(user -> {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(LocalDate.now());
            return userRepository.save(user);
        }).orElseThrow(() ->  {
            log.error("User with username: " + username + " not found");
            throw new UserNotFoundException("User with username: " + username + " not found");
        });

        return new UserPrincipal(foundUser);
    }

    @Override
    public User register(User userRequest) {
        String plainPassword = generatePassword();
        String encodedPassword = encodePassword(plainPassword);

        Role userRole = roleService.findRoleByName(RoleEnum.ROLE_MANAGER);

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(encodedPassword)
                .isActive(true)
                .isLocked(false)
                .joinDate(LocalDate.now())
                .roles(Set.of(userRole))
                .profileImageUrl(getProfileImageUrl())
                .build();

        log.info("new password: " + plainPassword);
        log.info("new encoded password: " + encodedPassword);

        userRole.setAuthorities(roleService.findAuthoritiesByRoles(Set.of(userRole)));

//        sendNewRegistrationEmail(user.getEmail(), plainPassword);

        return userRepository.save(user);
    }

    private void sendNewRegistrationEmail(String email, String password) {
        Email newRegistrationEmail = EmailService.sendNewRegistrationEmail(email, password);
        EmailSender.buildMailer().sendMail(newRegistrationEmail, true).onSuccess(() -> {
           log.info("cestitam, poslali ste mail");
        });
    }

    @Override
    public User login(User userRequest) {
        authenticate(userRequest);

        return userRepository.findUserByUsername(userRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public String getJwtToken(User user) {
        return jwtTokenProvider.generateJwtToken(new UserPrincipal(user));
    }

    private void authenticate(User userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRequest.getUsername(),
                userRequest.getPassword()
        ));
    }

    private String getProfileImageUrl() {
        return "";
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
