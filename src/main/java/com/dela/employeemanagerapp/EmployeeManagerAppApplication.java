package com.dela.employeemanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

import static com.dela.employeemanagerapp.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class EmployeeManagerAppApplication {

    public static void main(String[] args) {
        // make folder to store user profile images, now we store them locally
        new File(USER_FOLDER).mkdirs();
        SpringApplication.run(EmployeeManagerAppApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
