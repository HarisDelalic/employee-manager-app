package com.dela.employeemanagerapp.exception.domain;

public class PageNotFoundException extends RuntimeException {

    public PageNotFoundException(String message) {
        super(message);
    }
}
