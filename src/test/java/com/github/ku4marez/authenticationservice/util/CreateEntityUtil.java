package com.github.ku4marez.authenticationservice.util;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.commonlibraries.entity.dto.UserDTO;
import com.github.ku4marez.commonlibraries.entity.enums.Role;

import java.util.List;

public class CreateEntityUtil {

    private CreateEntityUtil() {
    }

    // User Constants
    public static final String DEFAULT_ID = "123";
    public static final String DEFAULT_FIRST_NAME = "John";
    public static final String DEFAULT_LAST_NAME = "Doe";
    public static final String DEFAULT_EMAIL = "john.doe@example.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final Role PATIENT_ROLE = Role.PATIENT;

    public static final String OTHER_FIRST_NAME = "Jane";
    public static final String OTHER_LAST_NAME = "Smith";
    public static final String OTHER_EMAIL = "jane.smith@example.com";
    public static final String OTHER_PASSWORD = "password2";
    public static final Role ADMIN_ROLE = Role.ADMIN;

    public static UserEntity createDefaultUserEntity() {
        return createUserEntity(DEFAULT_ID, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD, PATIENT_ROLE);
    }

    public static UserDTO createDefaultUserDTO() {
        return UserDTO.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .role(PATIENT_ROLE)
                .build();
    }

    public static UserDTO createOtherUserDTO() {
        return createUserDTO(OTHER_FIRST_NAME, OTHER_LAST_NAME, OTHER_EMAIL, OTHER_PASSWORD, ADMIN_ROLE);
    }

    public static UserEntity createUserEntity(String id, String firstName, String lastName, String email, String password, Role role) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    public static UserDTO createUserDTO(String firstName, String lastName, String email, String password, Role role) {
        return new UserDTO(firstName, lastName, email, password, role);
    }

    public static List<UserEntity> createUserEntityList() {
        return List.of(
                createUserEntity("1", DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD, PATIENT_ROLE),
                createUserEntity("2", OTHER_FIRST_NAME, OTHER_LAST_NAME, OTHER_EMAIL, OTHER_PASSWORD, ADMIN_ROLE)
        );
    }

    public static List<UserDTO> createUserDTOList() {
        return List.of(
                createUserDTO(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD, PATIENT_ROLE),
                createUserDTO(OTHER_FIRST_NAME, OTHER_LAST_NAME, OTHER_EMAIL, OTHER_PASSWORD, ADMIN_ROLE)
        );
    }
}
