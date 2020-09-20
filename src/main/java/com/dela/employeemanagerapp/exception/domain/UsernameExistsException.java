package com.dela.employeemanagerapp.exception.domain;

public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(String message) {
        super(message);
    }
}
