package com.dela.employeemanagerapp.controller;

import com.dela.employeemanagerapp.constant.SecurityConstant;
import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.AuthorityEnum;
import com.dela.employeemanagerapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.dela.employeemanagerapp.constant.FileConstant.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/registration", headers = "Accept=application/json")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.register(user, Collections.<Role>emptySet()), HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<User> createUserFromInsideApp(
            @Valid @RequestBody User user,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<User>(userService.createUserFromInsideApp(user, image), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<User> update(
            @RequestBody User user,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<User>(userService.updateUser(user, image), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority(AuthorityEnum.USER_DELETE)")
    public ResponseEntity<String> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateProfileImage/{username}")
    public ResponseEntity<User> updateProfileImage(
            @RequestParam String userame,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<User>(userService.updateProfileImage(userame, image), HttpStatus.OK);
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

    @GetMapping(value = "/image/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTemporaryProfileImage(@PathVariable String username) throws IOException {
        return userService.getTemporaryProfileImage(username);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestBody User requestUser) {
        User user = userService.login(requestUser);
        String jwtToken = userService.getJwtToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtToken);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
