package com.dela.employeemanagerapp.filter;

import com.dela.employeemanagerapp.constant.SecurityConstant;
import com.dela.employeemanagerapp.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.dela.employeemanagerapp.constant.SecurityConstant.*;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (methodIsOption(request)) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeaderExists(authorizationHeader)) {
                // removing word BEARER, what remains is  token itself
                String jwtToken = authorizationHeader.substring(TOKEN_PREFIX.length());
                String username = jwtTokenProvider.getSubject(jwtToken);

                if (tokenIsValidAndAuthenticationNotSet(username, jwtToken)) {
                    setSpringSecurityAuthentication(username, jwtToken, request);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean tokenIsValidAndAuthenticationNotSet(String username, String jwtToken) {
        return jwtTokenProvider.isJwtTokenValid(username, jwtToken) &&
                SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void setSpringSecurityAuthentication(String username, String jwtToken, HttpServletRequest request) {
        Set<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromJwtToken(jwtToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean methodIsOption(HttpServletRequest request) {
        return OPTIONS_HTTP_METHOD.equalsIgnoreCase(request.getMethod());
    }

    private boolean authorizationHeaderExists(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX);
    }
}

