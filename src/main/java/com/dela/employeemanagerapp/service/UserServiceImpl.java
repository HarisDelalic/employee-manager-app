package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import com.dela.employeemanagerapp.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dela.employeemanagerapp.constant.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@Service
@Qualifier("userServiceImpl")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserFactory userFactory;
    private final JWTTokenProvider jwtTokenProvider;
    private final Authenticator authenticator;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User foundUser = userRepository.findUserByUsername(username).map(user -> {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(LocalDate.now());
            return userRepository.save(user);
        }).orElseThrow(() -> {
            log.error("User with username: " + username + " not found");
            throw new UserNotFoundException("User with username: " + username + " not found");
        });

        return new UserPrincipal(foundUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not Found"));
    }

    @Override
    public User register(User userRequest, Set<Role> roles) {
        Set<Role> userRoles = roles.isEmpty() ?
                Set.of(roleService.findRoleByName(RoleEnum.ROLE_USER)) :
                roles;

        setUserAuthorities(userRoles);

        User user = userFactory.createNewUser(userRequest, userRoles);

        return userRepository.save(user);
    }

    private void setUserAuthorities(Set<Role> userRoles) {
        for (Role role : userRoles) {
            role.setAuthorities(roleService.findAuthoritiesByRole(role));
        }
    }

    @Override
    @Transactional
    public User createUserFromInsideApp(User user, MultipartFile image) throws IOException {
        User withProfileImage = saveProfileImageOnSystem(user, image);

        Set<RoleEnum> userRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        user.setRoles(roleService.findRolesByNameIn(userRoles));

        return register(withProfileImage, user.getRoles());
    }

    @Override
    public User updateUser(String oldUsername, User user, MultipartFile image) throws IOException {
        if (image != null) {
            saveProfileImageOnSystem(user, image);
        }

        Set<RoleEnum> userRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        user.setRoles(roleService.findRolesByNameIn(userRoles));

        User updatedUser = userFactory.updateUser(oldUsername, user, user.getRoles());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public User resetPassword(String email) {
        User user = userRepository.findUserByEmail(email).map(foundUser -> {
            userFactory.resetPassword(foundUser);
            return userRepository.save(foundUser);
        }).orElseThrow(() -> {
            throw new UserNotFoundException("User not Found");
        });
        return user;
    }

    @Override
    public User updateProfileImage(String username, MultipartFile image) throws IOException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isPresent()) {
            User foundUser = user.get();
            saveProfileImageOnSystem(foundUser, image);
            return foundUser;
        } else {
            throw new UserNotFoundException("User not Found");
        }
    }

    @Override
    public byte[] getTemporaryProfileImage(String username) throws IOException {
        URL tempProfileImageUrl = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = tempProfileImageUrl.openStream()){
            IOUtils.copy(inputStream, outputStream);
        }
        return outputStream.toByteArray();
    }

    @Override
    public List<User> findByUsernameOrEmailOrLastNameOrFirstName(String searchTerm) {
        return userRepository.findUsers(searchTerm);
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
        authenticator.authenticate(userRequest);
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private User saveProfileImageOnSystem(User user, MultipartFile profileImage) throws IOException, RuntimeException {
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new RuntimeException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
        return user;
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }
}
