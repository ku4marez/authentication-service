package com.github.ku4marez.authenticationservice.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomSearchRepository<T> {
    Optional<T> findByEmailCaseInsensitive(String email);
    Page<T> searchByName(String name, Pageable pageable);
}
