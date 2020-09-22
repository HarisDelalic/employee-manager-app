package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.User;

public interface UserService {
    User register(User user);
    User login(User user);
    String getJwtToken(User user);
}
