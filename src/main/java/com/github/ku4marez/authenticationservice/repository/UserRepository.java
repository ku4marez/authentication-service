package com.github.ku4marez.authenticationservice.repository;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.authenticationservice.repository.custom.CustomSearchRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, CustomSearchRepository<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
}