package com.github.ku4marez.authenticationservice.service.impl;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.authenticationservice.repository.UserRepository;
import com.github.ku4marez.authenticationservice.service.AuthService;
import com.github.ku4marez.authenticationservice.service.UserDetailsServiceImpl;
import com.github.ku4marez.commonlibraries.dto.response.AuthResponse;
import com.github.ku4marez.commonlibraries.entity.enums.Role;
import com.github.ku4marez.commonlibraries.exception.RefreshTokenException;
import com.github.ku4marez.commonlibraries.exception.UserAlreadyExistsException;
import com.github.ku4marez.commonlibraries.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                           UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void register(String username, String password) {
        if (userRepository.findByEmail(username).isPresent()) {
            throw new UserAlreadyExistsException(username);
        }

        // Create and save the new user
        UserEntity user = new UserEntity();
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Generate tokens
        String accessToken = jwtUtil.generateToken(userDetails.getUsername(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public String refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        if (!jwtUtil.isTokenValid(refreshToken, username)) {
            throw new RefreshTokenException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return jwtUtil.generateToken(username, roles);
    }
}
