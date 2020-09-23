package com.dela.employeemanagerapp.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailFactory {
    static final String SUBJECT = "Your Password";

    public Email createRegistrationEmail(String email, String password) {
        return EmailBuilder.startingBlank()
                .from("From", "delalicharis@gmail.com")
                .to("To", email)
                .withSubject(SUBJECT)
                .withPlainText("Your Password is: " + password)
                .buildEmail();
    }
}
