package com.dela.employeemanagerapp.exception.domain;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message) {
        super(message);
    }
}
