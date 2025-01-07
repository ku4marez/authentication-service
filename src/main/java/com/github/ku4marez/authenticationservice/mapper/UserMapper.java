package com.github.ku4marez.authenticationservice.mapper;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.commonlibraries.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(UserEntity userEntity);

    UserEntity toEntity(UserDTO userDTO);

    void updateEntityFromDto(UserDTO userDTO, @MappingTarget UserEntity userEntity);

}
