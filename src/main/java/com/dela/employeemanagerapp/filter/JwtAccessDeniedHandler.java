package com.dela.employeemanagerapp.filter;

import com.dela.employeemanagerapp.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final MessageSource securityMessageSource;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        HttpResponse httpResponse = accessDeniedResponse(request);

        generateAccessDeniedHttpServletResponse(response, httpResponse);
    }

    private HttpResponse accessDeniedResponse(HttpServletRequest request) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
                .message(securityMessageSource.getMessage("security.access_denied_message", null,
                        Locale.forLanguageTag(request.getHeader("Accept-Language"))))
                .reason("simply access denied ... ")
                .build();
    }

    private void generateAccessDeniedHttpServletResponse(HttpServletResponse httpServletResponse, HttpResponse httpResponse)
            throws IOException {
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        OutputStream responseOutputStream = httpServletResponse.getOutputStream();
        objectMapper.writeValue(responseOutputStream, httpResponse);
        responseOutputStream.flush();
    }
}
