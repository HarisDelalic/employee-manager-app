package com.dela.employeemanagerapp.exception;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserExceptionHandlerTest {

    @Mock(name = "exceptionMessageSource")
    MessageSource exceptionMessageSource;

    @InjectMocks
    UserExceptionHandler exceptionHandler;

    @Test
    void handleUserNotFoundException() {
        Mockito.when(exceptionMessageSource.getMessage("exception_message_user_not_found",
                null,
                LocaleContextHolder.getLocale())).thenReturn("Benutzer wurde nicht gefunden");

        ResponseEntity<HttpResponse> response =
                exceptionHandler.handleUserNotFoundException(new UserNotFoundException("User not Found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getHttpStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getHttpStatusCode());
        assertEquals("User not Found", response.getBody().getMessage());
        assertEquals("Benutzer wurde nicht gefunden", response.getBody().getReason());
    }
}