package com.github.ku4marez.authenticationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ku4marez.commonlibraries.entity.dto.UserDTO;
import com.github.ku4marez.commonlibraries.entity.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDTOJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSerialization() throws Exception {
        UserDTO user = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("password")
                .role(Role.ADMIN)
                .build();

        String json = objectMapper.writeValueAsString(user);
        System.out.println(json);

        assertEquals("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"password\",\"role\":\"ADMIN\"}", json);
    }

    @Test
    void testDeserialization() throws Exception {
        String json = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "password": "password",
                    "role": "ADMIN"
                }
                """;

        UserDTO user = objectMapper.readValue(json, UserDTO.class);
        assertEquals("John", user.firstName());
        assertEquals("Doe", user.lastName());
    }
}
