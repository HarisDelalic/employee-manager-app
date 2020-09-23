package com.dela.employeemanagerapp.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordFactory {

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
