package com.dela.employeemanagerapp.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dela.employeemanagerapp.constant.SecurityConstant.*;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    MessageSource messageSource;


    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(MY_COMPANY)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(DateConverter.convertToDateAndAddDaysOffset(LocalDateTime.now()))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .toArray(String[]::new);
    }

    public Authentication getAuthentication(String username, Set<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isJwtTokenValid(String username, String token) {
        return StringUtils.isNotBlank(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        JWTVerifier verifier = getJwtVerifier();

        try {
            Date expireAt = verifier.verify(token).getExpiresAt();
            return new Date().after(expireAt);

        } catch (TokenExpiredException exception) {
            return true;
        }

    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJwtVerifier();

        return verifier.verify(token).getSubject();
    }

    public Set<GrantedAuthority> getAuthoritiesFromJwtToken(String jwtToken) {
        JWTVerifier verifier = getJwtVerifier();

        List<String> claims = verifier.verify(jwtToken).getClaim(AUTHORITIES).asList(String.class);

        return claims.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }


    private JWTVerifier getJwtVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(MY_COMPANY).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(messageSource
                    .getMessage("security.token_cant_be_verified", null, LocaleContextHolder.getLocale()));
        }
        return verifier;
    }

    private static class DateConverter {

        public static Date convertToDateAndAddDaysOffset(LocalDateTime dateToConvert) {
            LocalDateTime withDaysOffset = dateToConvert.plusDays(EXPIRATION_DAYS);
            return convertToDate(withDaysOffset);
        }

        public static Date convertToDate(LocalDateTime dateToConvert) {
            return java.util.Date
                    .from(dateToConvert.atZone(ZoneId.systemDefault())
                            .toInstant());
        }
    }
}
