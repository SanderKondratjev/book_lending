package com.lending.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lending.book.dto.LoginDto;
import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.exception.GlobalExceptionHandler;
import com.lending.book.repository.UserRepository;
import com.lending.book.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtUtil = mock(JwtUtil.class);
        AuthController authController = new AuthController(userRepository, jwtUtil);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setRole(Role.BORROWER);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("user1", "BORROWER")).thenReturn("jwt-token");

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user1");
        loginDto.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsInvalid() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setRole(Role.LENDER);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user1");
        loginDto.setPassword("wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("nonexistent");
        loginDto.setPassword("any");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}
