package com.dela.employeemanagerapp.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dela.employeemanagerapp.domain.HttpResponse;
import com.dela.employeemanagerapp.exception.domain.EmailExistsException;
import com.dela.employeemanagerapp.exception.domain.EmailNotFoundException;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import com.dela.employeemanagerapp.exception.domain.UsernameExistsException;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
//    TODO following should be i18n, some of them are, look at exception_messages, puno ih je
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ERROR_PATH = "/error";

    private final MessageSource exceptionMessageSource;

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(DisabledException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "exception_message_account_disabled");

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "exception_message_incorrect_credentials");

        return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException ex) {
        HttpResponse httpResponse =
                createHttpResponse(FORBIDDEN, ex, NOT_ENOUGH_PERMISSION);

        return new ResponseEntity<>(httpResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(LockedException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "exception_message_account_disabled");

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException ex) {
        HttpResponse httpResponse =
                createHttpResponse(UNAUTHORIZED, ex, "exception_message_token_expired");

        return new ResponseEntity<>(httpResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistsException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "EMAIL EXISTS");

        return new ResponseEntity<>(httpResponse, BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistsException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "USERNAME EXISTS");

        return new ResponseEntity<>(httpResponse, BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException ex) {
        HttpResponse httpResponse =
                createHttpResponse(BAD_REQUEST, ex, "EMAIL NOT FOUND");

        return new ResponseEntity<>(httpResponse, BAD_REQUEST);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> handleUserNotFoundException(UserNotFoundException ex) {

        HttpResponse httpResponse =
                createHttpResponse(HttpStatus.NOT_FOUND, ex, "exception_message_user_not_found");

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    private HttpResponse createHttpResponse(HttpStatus status, Exception ex, String message_code) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(status.value())
                .httpStatus(status)
                .message(ex.getMessage())
                .reason(exceptionMessageSource.getMessage(message_code,
                        null,
                        LocaleContextHolder.getLocale()))
                .build();

        return httpResponse;
    }
}
