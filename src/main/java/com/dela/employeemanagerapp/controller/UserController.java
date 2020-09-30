package com.dela.employeemanagerapp.controller;

import com.dela.employeemanagerapp.constant.SecurityConstant;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.dela.employeemanagerapp.constant.FileConstant.FORWARD_SLASH;
import static com.dela.employeemanagerapp.constant.FileConstant.USER_FOLDER;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<List<User>> findByUsernameOrEmailOrLastNameOrFirstName(
            @RequestParam("searchTerm") String searchTerm) {
        return new ResponseEntity<List<User>>(userService.
                findByUsernameOrEmailOrLastNameOrFirstName(searchTerm), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/registration", headers = "Accept=application/json")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User registerUser = userService.register(user, Collections.<Role>emptySet());
        HttpHeaders headers = saveJwtTokenToHeaders(registerUser);
        return new ResponseEntity<>(registerUser, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<User> createUserFromInsideApp(
            @RequestParam("user") String userAsJson,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        User user = objectMapper.readValue(userAsJson, User.class);
        return new ResponseEntity<User>(userService.createUserFromInsideApp(user, image), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<User> update(
            @RequestParam("username") String oldUsername,
            @RequestParam("user") String userAsJson,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        User user = objectMapper.readValue(userAsJson, User.class);
        return new ResponseEntity<User>(userService.updateUser(oldUsername, user, image), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/updateProfileImage/{username}")
    public ResponseEntity<User> updateProfileImage(
            @RequestParam String username,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<User>(userService.updateProfileImage(username, image), HttpStatus.OK);
    }

    @PutMapping(value = "/resetpassword/{email}")
    public ResponseEntity<User> resetPassword(@PathVariable String email) {
        return new ResponseEntity<User>(userService.resetPassword(email), HttpStatus.OK);
    }

    @GetMapping(value = "/image/{username}/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable String username,
                                  @PathVariable String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
    }

    @GetMapping(value = "/image/profile/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTemporaryProfileImage(@PathVariable String username) throws IOException {
        return userService.getTemporaryProfileImage(username);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestBody User requestUser) {
        User user = userService.login(requestUser);
        HttpHeaders headers = saveJwtTokenToHeaders(user);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }

    private HttpHeaders saveJwtTokenToHeaders(User user) {
        String jwtToken = userService.getJwtToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtToken);
        return headers;
    }
}
