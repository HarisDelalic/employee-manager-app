package com.dela.employeemanagerapp.filter;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    MessageSource securityMessageSource;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        httpResponse =  HttpResponse.builder()
                .timeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .httpStatus(HttpStatus.FORBIDDEN)
                .httpStatusCode(HttpStatus.FORBIDDEN.value())
                .reason("simply forbidden ... ")
                .build();
    }

    @Test
    void commence() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "en");

        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = mock(AuthenticationException.class);

        jwtAuthenticationEntryPoint.commence(request, response, exception);

        verify(objectMapper).writeValue(response.getOutputStream(), httpResponse);

        assertEquals(response.getContentType(), APPLICATION_JSON_VALUE);
        assertEquals(response.getStatus(), HttpStatus.FORBIDDEN.value());
    }
}