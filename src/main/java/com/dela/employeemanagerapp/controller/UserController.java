package com.dela.employeemanagerapp.controller;

import com.dela.employeemanagerapp.constant.SecurityConstant;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/registration", headers="Accept=application/json")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
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
