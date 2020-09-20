package com.dela.employeemanagerapp.exception.domain;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
