package com.dela.employeemanagerapp.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityEnumTest {

    @Test
    public void testGetValue() {
        assertEquals(AuthorityEnum.USER_READ.getValue(), "user:read");
        assertEquals(AuthorityEnum.USER_UPDATE.getValue(), "user:update");
        assertEquals(AuthorityEnum.USER_CREATE.getValue(), "user:create");
        assertEquals(AuthorityEnum.USER_DELETE.getValue(), "user:delete");
    }
}