package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Authenticator {

    private final AuthenticationManager authenticationManager;

    public void authenticate(User userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRequest.getUsername(),
                userRequest.getPassword()
        ));
    }
}