package com.dela.employeemanagerapp.constant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityConstantTest {
    private static final String SECURITY_PREFIX = "security.";

    ResourceBundleMessageSource messageSource;

    @BeforeEach
    void setUp() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("security_messages");
        messageSource.setDefaultEncoding("UTF8");
    }

    @Test
    public void testSecurityConstantsEN() {
        assertEquals("Token can not be verified",
                messageSource.getMessage(SECURITY_PREFIX + "token_cant_be_verified", null, Locale.ENGLISH));
        assertEquals("You need to log in to access this page",
                messageSource.getMessage(SECURITY_PREFIX + "forbidden_message", null, Locale.ENGLISH));
        assertEquals("You do not have permission to access this page",
                messageSource.getMessage(SECURITY_PREFIX + "access_denied_message", null, Locale.ENGLISH));
    }

    @Test
    public void testSecurityConstantsDE() {
        assertEquals("Token kann nicht überprüft werden",
                messageSource.getMessage(SECURITY_PREFIX + "token_cant_be_verified", null, Locale.GERMAN));
        assertEquals("Sie müssen sich anmelden, um auf diese Seite zugreifen zu können",
                messageSource.getMessage(SECURITY_PREFIX + "forbidden_message", null, Locale.GERMAN));
        assertEquals("Sie haben keine Berechtigung, auf diese Seite zuzugreifen",
                messageSource.getMessage(SECURITY_PREFIX + "access_denied_message", null, Locale.GERMAN));
    }

}