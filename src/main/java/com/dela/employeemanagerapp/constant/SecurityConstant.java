package com.dela.employeemanagerapp.constant;

public class SecurityConstant {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String MY_COMPANY = "My Company";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {
            "/users/login",
            "/users/registration",
            "/users/resetpassword/**",
            "/users/image/**" };
}
