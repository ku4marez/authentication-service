package com.github.ku4marez.authenticationservice.controller;

import com.github.ku4marez.authenticationservice.service.AuthService;
import com.github.ku4marez.commonlibraries.entity.dto.request.AuthRequest;
import com.github.ku4marez.commonlibraries.entity.dto.request.RefreshTokenRequest;
import com.github.ku4marez.commonlibraries.entity.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        authService.register(authRequest.email(), authRequest.password());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest.email(), authRequest.password());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String newAccessToken = authService.refreshToken(refreshTokenRequest.refreshToken());
        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshTokenRequest.refreshToken()));
    }
}
