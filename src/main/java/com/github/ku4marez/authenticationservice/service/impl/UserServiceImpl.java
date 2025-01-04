package com.github.ku4marez.authenticationservice.service.impl;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.authenticationservice.mapper.UserMapper;
import com.github.ku4marez.authenticationservice.repository.UserRepository;
import com.github.ku4marez.authenticationservice.service.UserService;
import com.github.ku4marez.commonlibraries.entity.dto.UserDTO;
import com.github.ku4marez.commonlibraries.entity.exception.UserNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.toEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return modelMapper.toDto(savedUser);
    }

    @Override
    @Cacheable(value = "users", key = "'user_' + #email")
    public UserDTO findUserByEmailCaseSensitive(String email) {
        UserEntity userEntity = userRepository.findByEmailCaseInsensitive(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return modelMapper.toDto(userEntity);
    }

    @Override
    @Cacheable(value = "users", key = "'user_' + #id")
    public UserDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(modelMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Cacheable(value = "users", key = "'users_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10); // Provide a default Pageable
        }
        Page<UserEntity> usersPage = userRepository.findAll(pageable);

        return usersPage.map(modelMapper::toDto);
    }

    @Override
    @CachePut(value = "users", key = "'user_' + #id")
    @CacheEvict(value = "userDetails", key = "#userDTO.email")
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        modelMapper.updateEntityFromDto(userDTO, existingUser);

        UserEntity updatedUser = userRepository.save(existingUser);
        return modelMapper.toDto(updatedUser);
    }

    @Override
    @CacheEvict(value = {"users", "userDetails"}, key = "'user_' + #id")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
