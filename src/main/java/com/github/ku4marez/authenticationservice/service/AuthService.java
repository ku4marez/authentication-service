package com.github.ku4marez.authenticationservice.service;

import com.github.ku4marez.commonlibraries.entity.dto.response.AuthResponse;

public interface AuthService {
    void register(String username, String password);
    AuthResponse login(String username, String password);
    String refreshToken(String token);
}
