package com.lending.book.controller;

import com.lending.book.dto.LoginDto;
import com.lending.book.security.JwtUtil;
import com.lending.book.entity.User;
import com.lending.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Optional<User> userOptional = userRepository.findByUsername(loginDto.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
            String token = jwtUtil.generateToken(loginDto.getUsername(), user.getRole().name());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
