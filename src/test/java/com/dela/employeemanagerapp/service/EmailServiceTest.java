package com.dela.employeemanagerapp.service;

import org.junit.jupiter.api.Test;
import org.simplejavamail.api.email.Email;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Test
    void sendNewRegistrationEmail() {
        Email email = EmailService
                .sendNewRegistrationEmail("dela@dela.com", "password");
        assertThat(email.getFromRecipient().getAddress()).isEqualTo("delalicharis@gmail.com");
        assertThat(email.getRecipients()).hasSize(1);
        assertThat(email.getRecipients().get(0).getAddress()).isEqualTo("dela@dela.com");
        assertThat(email.getSubject())
                .isEqualTo(EmailService.SUBJECT);
        assertThat(email.getPlainText()).isEqualTo("Your Password is: password");
    }
}