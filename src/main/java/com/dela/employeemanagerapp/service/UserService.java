package com.dela.employeemanagerapp.service;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface UserService {
    User register(User user, Set<Role> roles);
    User login(User user);
    String getJwtToken(User user);
    User createUserFromInsideApp(User user, MultipartFile image) throws IOException;
    User updateUser(User user, MultipartFile image) throws IOException;
    void deleteUser(Long id);
    void resetPassword(String email);
    User updateProfileImage(String username, MultipartFile image) throws IOException;
}
