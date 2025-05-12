package com.lending.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lending.book.dto.UserDto;
import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        UserController controller = new UserController(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setPassword("password123");
        userDto.setEmail("user1@example.com");
        userDto.setRole("borrower");

        User savedUser = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .role(Role.BORROWER)
                .passwordHash(new BCryptPasswordEncoder().encode("password123"))
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.role").value("BORROWER"));
    }
}
