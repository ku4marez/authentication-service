package com.github.ku4marez.authenticationservice.controller;

import com.github.ku4marez.authenticationservice.constant.TestConstants;
import com.github.ku4marez.authenticationservice.constant.UserRole;
import com.github.ku4marez.authenticationservice.dto.UserDTO;
import com.github.ku4marez.authenticationservice.service.UserService;
import com.github.ku4marez.authenticationservice.util.CreateEntityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private UserDTO userDTO;

    @BeforeEach
    public void setup() {
        mongoTemplate.getDb().drop();
        userDTO = CreateEntityUtil.createDefaultUserDTO();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "john.doe@example.com",
                                "password": "password",
                                "role": "PATIENT"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(CreateEntityUtil.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.email").value(CreateEntityUtil.DEFAULT_EMAIL));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(CreateEntityUtil.DEFAULT_ID)).thenReturn(userDTO);

        mockMvc.perform(get("/users/{id}", CreateEntityUtil.DEFAULT_ID)
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(CreateEntityUtil.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.email").value(CreateEntityUtil.DEFAULT_EMAIL));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        when(userService.findUserByEmailCaseSensitive(CreateEntityUtil.DEFAULT_EMAIL)).thenReturn(userDTO);

        mockMvc.perform(get("/users/search")
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN))
                        .param("email", CreateEntityUtil.DEFAULT_EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(CreateEntityUtil.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.email").value(CreateEntityUtil.DEFAULT_EMAIL));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = CreateEntityUtil.createUserDTOList();
        Page<UserDTO> userPage = new PageImpl<>(users);

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].firstName").value(CreateEntityUtil.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.content[1].firstName").value(CreateEntityUtil.OTHER_FIRST_NAME));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(eq(CreateEntityUtil.DEFAULT_ID), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/{id}", CreateEntityUtil.DEFAULT_ID)
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstName": "Jane",
                                "lastName": "Smith",
                                "email": "jane.smith@example.com",
                                "password": "password2",
                                "role": "ADMIN"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(CreateEntityUtil.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.email").value(CreateEntityUtil.DEFAULT_EMAIL));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(CreateEntityUtil.DEFAULT_ID);

        mockMvc.perform(delete("/users/{id}", CreateEntityUtil.DEFAULT_ID)
                        .with(SecurityMockMvcRequestPostProcessors.user(TestConstants.USERNAME).roles(UserRole.ADMIN)))
                .andExpect(status().isNoContent());
    }
}
