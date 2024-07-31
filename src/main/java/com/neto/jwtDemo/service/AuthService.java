package com.neto.jwtDemo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neto.jwtDemo.Auth.AuthResponse;
import com.neto.jwtDemo.Auth.LoginRequest;
import com.neto.jwtDemo.Auth.RegisterRequest;
import com.neto.jwtDemo.model.UserModel;
import com.neto.jwtDemo.repository.UserRepository;
import com.neto.jwtDemo.user.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public AuthResponse login(LoginRequest request) {
                authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                                                request.getPassword()));
                UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
                String token = jwtService.getToken(user);
                return AuthResponse.builder()
                                .token(token)
                                .build();
        }

        public AuthResponse register(RegisterRequest request) {
                UserModel user = UserModel.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .country(request.getCountry())
                                .role(Role.USER)
                                .build();
                userRepository.save(user);
                return AuthResponse.builder()
                                .token(jwtService.getToken(user))
                                .build();
        }
}