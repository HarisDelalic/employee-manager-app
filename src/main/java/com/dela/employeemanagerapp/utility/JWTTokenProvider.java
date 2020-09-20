package com.dela.employeemanagerapp.utility;

import ch.qos.logback.classic.pattern.DateConverter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dela.employeemanagerapp.constant.SecurityConstant;
import com.dela.employeemanagerapp.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

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

    public Set<GrantedAuthority> getClaimsFromJwtToken(String jwtToken) {
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
