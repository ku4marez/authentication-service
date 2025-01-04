package com.github.ku4marez.authenticationservice.repository;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.authenticationservice.util.CreateEntityUtil;
import com.github.ku4marez.commonlibraries.entity.entity.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabaseBeforeEachTest() {
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanDatabaseAfterEachTest() {
        userRepository.deleteAll();
    }

    @Test
    void testFindById_Success() {
        UserEntity user = CreateEntityUtil.createDefaultUserEntity();
        userRepository.save(user);

        Optional<UserEntity> result = userRepository.findById(CreateEntityUtil.DEFAULT_ID);

        assertTrue(result.isPresent());
        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.get().getFirstName());
    }

    @Test
    void testFindByEmailCaseInsensitive_Success() {
        UserEntity user = CreateEntityUtil.createDefaultUserEntity();
        userRepository.save(user);

        Optional<UserEntity> result = userRepository.findByEmailCaseInsensitive(CreateEntityUtil.DEFAULT_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.get().getFirstName());
    }

    @Test
    void testSearchByName_Success() {
        UserEntity user1 = CreateEntityUtil.createUserEntity("1", "John", "Davskin", "john.davskin@example.com", "password", Role.ADMIN);
        UserEntity user2 = CreateEntityUtil.createUserEntity("2", "John", "Bobskin", "John.bobskin@example.com", "password", Role.ADMIN);
        userRepository.save(user1);
        userRepository.save(user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> result = userRepository.searchByName("John", pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("John", result.getContent().get(1).getFirstName());
    }

    @Test
    void testFindByEmailCaseInsensitive_NotFound() {
        Optional<UserEntity> result = userRepository.findByEmailCaseInsensitive("nonexistent@example.com");

        assertFalse(result.isPresent());
    }
}
