package com.dela.employeemanagerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSender {
    private final EmailFactory emailFactory;
    // mailtrap works for dev and test, not prod, does not send actual emails, just simulates
    // TODO: following values should be according to that added to application-dev.properties and ...-test.properties
    private static final String SMTP = "smtp.mailtrap.io";
    private static final int PORT = 2525;
    private static final String USERNAME = "e8fff7deaac3a0";
    private static final String PASSWORD = "52b3d64efdb439";

    public Mailer buildEmailSender() {
        return MailerBuilder
                .withSMTPServer(SMTP, PORT, USERNAME, PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP)
                .buildMailer();
    }

    public void createAndSendNewUserEmail(String email, String plainPassword) {
        Email registrationEmail = emailFactory.createRegistrationEmail(email, plainPassword);
        buildEmailSender().sendMail(registrationEmail, true).onSuccess(() -> {
            log.info("User with email: " + email +"successfully notified");
        });
    }
}
