package com.dela.employeemanagerapp.controller;

import com.dela.employeemanagerapp.domain.Role;
import com.dela.employeemanagerapp.domain.User;
import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import com.dela.employeemanagerapp.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private WebApplicationContext context;

    User fromRequest;
    User fromResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(value = "spring")
    @Test
    void register() throws Exception {

        fromRequest = User.builder()
                .username("username")
                .email("email@emial.com")
                .firstName("firstName")
                .lastName("lastname")
                .build();

        fromResponse = User.builder()
                .username(fromRequest.getUsername())
                .email(fromRequest.getEmail())
                .firstName(fromRequest.getFirstName())
                .lastName(fromRequest.getLastName())
                .isActive(true)
                .isLocked(false)
                .roles(Set.of(Role.of(RoleEnum.ROLE_USER)))
                .build();

        when(userService.register(fromRequest)).thenReturn(fromResponse);

        mockMvc.perform(post("/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fromRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(fromResponse.getUsername())))
                .andExpect(jsonPath("$.email", is(fromResponse.getEmail())))
                .andExpect(jsonPath("$.firstName", is(fromResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(fromResponse.getLastName())))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.locked", is(false)))
                .andExpect(jsonPath("$.roles[0].name", is(RoleEnum.ROLE_USER.name())));
    }
}