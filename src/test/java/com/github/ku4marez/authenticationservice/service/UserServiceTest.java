package com.github.ku4marez.authenticationservice.service;


import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.authenticationservice.mapper.UserMapper;
import com.github.ku4marez.authenticationservice.repository.UserRepository;
import com.github.ku4marez.authenticationservice.service.impl.UserServiceImpl;
import com.github.ku4marez.authenticationservice.util.CreateEntityUtil;
import com.github.ku4marez.commonlibraries.entity.dto.UserDTO;
import com.github.ku4marez.commonlibraries.entity.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserById() {
        UserEntity user = CreateEntityUtil.createDefaultUserEntity();
        UserDTO userDTO = CreateEntityUtil.createDefaultUserDTO();

        when(userRepository.findById(CreateEntityUtil.DEFAULT_ID)).thenReturn(Optional.of(user));
        when(modelMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(CreateEntityUtil.DEFAULT_ID);

        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.firstName());
        verify(userRepository, times(1)).findById(CreateEntityUtil.DEFAULT_ID);
        verify(modelMapper, times(1)).toDto(user);
    }

    @Test
    void testUserNotFoundException() {
        when(userRepository.findById(CreateEntityUtil.DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(CreateEntityUtil.DEFAULT_ID));
        verify(userRepository, times(1)).findById(CreateEntityUtil.DEFAULT_ID);
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = CreateEntityUtil.createDefaultUserDTO();
        UserEntity userEntity = CreateEntityUtil.createDefaultUserEntity();

        when(modelMapper.toEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.toDto(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.firstName());
        assertEquals(CreateEntityUtil.DEFAULT_LAST_NAME, result.lastName());
        verify(modelMapper, times(1)).toEntity(userDTO);
        verify(userRepository, times(1)).save(userEntity);
        verify(modelMapper, times(1)).toDto(userEntity);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(CreateEntityUtil.DEFAULT_ID);

        verify(userRepository, times(1)).deleteById(CreateEntityUtil.DEFAULT_ID);
    }

    @Test
    void testGetAllUsers() {
        Pageable pageable = PageRequest.of(0, 2);
        List<UserEntity> users = CreateEntityUtil.createUserEntityList();
        Page<UserEntity> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserDTO> userDTOs = CreateEntityUtil.createUserDTOList();

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(modelMapper.toDto(users.get(0))).thenReturn(userDTOs.get(0));
        when(modelMapper.toDto(users.get(1))).thenReturn(userDTOs.get(1));

        Page<UserDTO> result = userService.getAllUsers(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.getContent().get(0).firstName());
        assertEquals(CreateEntityUtil.OTHER_FIRST_NAME, result.getContent().get(1).firstName());
        verify(userRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(2)).toDto(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_Success() {
        UserEntity existingUser = CreateEntityUtil.createDefaultUserEntity();
        UserDTO userDTO = CreateEntityUtil.createOtherUserDTO();

        when(userRepository.findById(CreateEntityUtil.DEFAULT_ID)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);
        when(modelMapper.toDto(any(UserEntity.class))).thenReturn(userDTO);

        UserDTO updatedUser = userService.updateUser(CreateEntityUtil.DEFAULT_ID, userDTO);

        assertEquals(CreateEntityUtil.OTHER_FIRST_NAME, updatedUser.firstName());
        verify(userRepository, times(1)).findById(CreateEntityUtil.DEFAULT_ID);
        verify(modelMapper, times(1)).updateEntityFromDto(userDTO, existingUser);
        verify(userRepository, times(1)).save(existingUser);
        verify(modelMapper, times(1)).toDto(existingUser);
    }


    @Test
    void testFindUserByEmailCaseSensitive_Success() {
        UserEntity user = CreateEntityUtil.createDefaultUserEntity();
        UserDTO userDTO = CreateEntityUtil.createDefaultUserDTO();

        when(userRepository.findByEmailCaseInsensitive(CreateEntityUtil.DEFAULT_EMAIL)).thenReturn(Optional.of(user));
        when(modelMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findUserByEmailCaseSensitive(CreateEntityUtil.DEFAULT_EMAIL);

        assertEquals(CreateEntityUtil.DEFAULT_FIRST_NAME, result.firstName());
        verify(userRepository, times(1)).findByEmailCaseInsensitive(CreateEntityUtil.DEFAULT_EMAIL);
        verify(modelMapper, times(1)).toDto(user);
    }

    @Test
    void testFindUserByEmailCaseSensitive_UserNotFound() {
        when(userRepository.findByEmailCaseInsensitive(CreateEntityUtil.DEFAULT_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmailCaseSensitive(CreateEntityUtil.DEFAULT_EMAIL));
        verify(userRepository, times(1)).findByEmailCaseInsensitive(CreateEntityUtil.DEFAULT_EMAIL);
    }
}
