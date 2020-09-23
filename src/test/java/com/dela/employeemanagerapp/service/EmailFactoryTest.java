package com.dela.employeemanagerapp.service;

import org.junit.jupiter.api.Test;
import org.simplejavamail.api.email.Email;

import static org.assertj.core.api.Assertions.assertThat;

class EmailFactoryTest {

    @Test
    void sendNewRegistrationEmail() {
        EmailFactory emailFactory = new EmailFactory();

        Email email = emailFactory.createRegistrationEmail("dela@dela.com", "password");
        assertThat(email.getFromRecipient().getAddress()).isEqualTo("delalicharis@gmail.com");
        assertThat(email.getRecipients()).hasSize(1);
        assertThat(email.getRecipients().get(0).getAddress()).isEqualTo("dela@dela.com");
        assertThat(email.getSubject())
                .isEqualTo(EmailFactory.SUBJECT);
        assertThat(email.getPlainText()).isEqualTo("Your Password is: password");
    }
}