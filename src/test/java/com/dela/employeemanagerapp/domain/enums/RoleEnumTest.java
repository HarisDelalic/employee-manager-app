package com.dela.employeemanagerapp.domain.enums;

import org.junit.jupiter.api.Test;

import static com.dela.employeemanagerapp.domain.enums.RoleEnum.*;
import static org.assertj.core.api.Assertions.assertThat;

class RoleEnumTest {

    @Test
    public void testRoleUserAuthorities() {
        assertThat(ROLE_USER.getAuthorities()).contains(AuthorityEnum.USER_READ);
        assertThat(ROLE_USER.getAuthorities()).doesNotContain(AuthorityEnum.USER_UPDATE);
        assertThat(ROLE_USER.getAuthorities()).doesNotContain(AuthorityEnum.USER_CREATE);
        assertThat(ROLE_USER.getAuthorities()).doesNotContain(AuthorityEnum.USER_DELETE);
    }

    @Test
    public void testRoleHrAuthorities() {
        assertThat(ROLE_HR.getAuthorities()).contains(AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE);
        assertThat(ROLE_HR.getAuthorities()).doesNotContain(AuthorityEnum.USER_CREATE);
        assertThat(ROLE_HR.getAuthorities()).doesNotContain(AuthorityEnum.USER_DELETE);
    }

    @Test
    public void testRoleManagerAuthorities() {
        assertThat(ROLE_MANAGER.getAuthorities()).contains(AuthorityEnum.USER_READ, AuthorityEnum.USER_UPDATE);
        assertThat(ROLE_MANAGER.getAuthorities()).doesNotContain(AuthorityEnum.USER_CREATE);
        assertThat(ROLE_MANAGER.getAuthorities()).doesNotContain(AuthorityEnum.USER_DELETE);
    }

    @Test
    public void testRoleAdminAuthorities() {
        assertThat(ROLE_ADMIN.getAuthorities()).contains(
                AuthorityEnum.USER_READ,
                AuthorityEnum.USER_UPDATE,
                AuthorityEnum.USER_CREATE);
        assertThat(ROLE_MANAGER.getAuthorities()).doesNotContain(AuthorityEnum.USER_DELETE);
    }

    @Test
    public void testRoleSuperUserAuthorities() {
        assertThat(ROLE_SUPERUSER.getAuthorities()).contains(
                AuthorityEnum.USER_READ,
                AuthorityEnum.USER_UPDATE,
                AuthorityEnum.USER_CREATE,
                AuthorityEnum.USER_DELETE);
    }
}