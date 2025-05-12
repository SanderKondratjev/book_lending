package com.lending.book.controller;

import com.lending.book.dto.UserDto;
import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .passwordHash(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(Role.valueOf(userDto.getRole().toUpperCase()))
                .build();

        return ResponseEntity.ok(userRepository.save(user));
    }
}
