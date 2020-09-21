package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Authority;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
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

    @Override
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
        String password = generatePassword();
        String encodedPassword = encodePassword(password);

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

        log.info("new password: " + password);
        log.info("new encoded password: " + encodedPassword);

        userRole.setAuthorities(roleService.findAuthoritiesByRoles(Set.of(userRole)));

        return userRepository.save(user);
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
