package com.dela.employeemanagerapp.filter;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource securityMessageSource;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        HttpResponse httpResponse = forbiddenResponse(request);

        generateForbiddenHttpServletResponse(response, httpResponse);
    }

    private HttpResponse forbiddenResponse(HttpServletRequest request) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .httpStatus(HttpStatus.FORBIDDEN)
                .httpStatusCode(HttpStatus.FORBIDDEN.value())
                .message(securityMessageSource.getMessage("security.forbidden_message", null,
                        Locale.forLanguageTag(request.getHeader("Accept-Language"))))
                .reason("simply forbidden ... ")
                .build();
    }

    private void generateForbiddenHttpServletResponse(HttpServletResponse httpServletResponse, HttpResponse httpResponse)
            throws IOException {
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());

        OutputStream responseOutputStream = httpServletResponse.getOutputStream();
        objectMapper.writeValue(responseOutputStream, httpResponse);
        responseOutputStream.flush();
    }
}
