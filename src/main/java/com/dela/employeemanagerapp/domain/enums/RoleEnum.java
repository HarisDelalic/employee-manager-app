package com.dela.employeemanagerapp.domain.enums;

public enum RoleEnum {
    ROLE_USER   (AuthorityEnum.USER_READ),
    ROLE_HR     (AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE),
    ROLE_MANAGER(AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE),
    ROLE_ADMIN  (AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE, AuthorityEnum.USER_CREATE),
    ROLE_SUPERUSER(AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE,
            AuthorityEnum.USER_CREATE, AuthorityEnum.USER_DELETE);

    private AuthorityEnum[] authorities;

    RoleEnum(AuthorityEnum... authorities) {
        this.authorities = authorities;
    }

    public AuthorityEnum[] getAuthorities() {
        return authorities;
    }
}
