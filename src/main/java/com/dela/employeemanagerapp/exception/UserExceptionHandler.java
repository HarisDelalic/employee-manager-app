package com.dela.employeemanagerapp.exception;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.dela.employeemanagerapp.exception.domain.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource exceptionMessageSource;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> handleUserNotFoundException(UserNotFoundException ex) {

        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .reason(exceptionMessageSource.getMessage("exception_message_user_not_found",
                        null,
                        LocaleContextHolder.getLocale()))
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }
}
