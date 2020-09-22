package com.dela.employeemanagerapp.service;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailSender {
    // TODO: these values should got to application-dev and application-test properties
    // mailtrap is not used for prod, it does not send mails
    private static final String SMTP = "smtp.mailtrap.io";
    private static final int PORT = 2525;
    private static final String USERNAME = "e8fff7deaac3a0";
    private static final String PASSWORD = "hidden";

    public static Mailer buildMailer() {
        return MailerBuilder
                .withSMTPServer(SMTP, PORT, USERNAME, PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP)
                .buildMailer();
    }
}
