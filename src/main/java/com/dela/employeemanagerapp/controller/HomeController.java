package com.dela.employeemanagerapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class HomeController {

    @GetMapping("/hello")
    public String hello() {
        return "abc";
    }
}
