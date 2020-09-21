package com.dela.employeemanagerapp.controller;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.dela.employeemanagerapp.exception.domain.PageNotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ResponseEntity<HttpResponse> getError() {
        throw new PageNotFoundException("Page Not Found");
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
