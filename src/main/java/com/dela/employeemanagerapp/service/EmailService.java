package com.dela.employeemanagerapp.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;

public class EmailService {
    static final String SUBJECT = "Your Password";

    static Email sendNewRegistrationEmail(String email, String password) {
        return EmailBuilder.startingBlank()
                .from("From", "delalicharis@gmail.com")
                .to("To", email)
                .withSubject(SUBJECT)
                .withPlainText("Your Password is: " + password)
                .buildEmail();
    }
}
