package com.dela.employeemanagerapp.domain.enums;

public enum AuthorityEnum {
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    private final String value;

    AuthorityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
