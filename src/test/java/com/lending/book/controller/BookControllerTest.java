package com.lending.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lending.book.dto.BookDto;
import com.lending.book.entity.Book;
import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.repository.BookRepository;
import com.lending.book.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    private MockMvc mockMvc;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        userRepository = mock(UserRepository.class);
        BookController controller = new BookController(bookRepository, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnAllBooksWhenNoTitleIsProvided() throws Exception {
        Book book = new Book();
        book.setTitle("Book A");

        when(bookRepository.findAll()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book A"));
    }

    @Test
    void shouldReturnBooksByTitleSearch() throws Exception {
        Book book = new Book();
        book.setTitle("Java Guide");

        when(bookRepository.findByTitleContainingIgnoreCase("java")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books?title=java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Guide"));
    }

    @Test
    void shouldAddBookWhenUserIsLender() throws Exception {
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn("user1");

            SecurityContext context = mock(SecurityContext.class);
            when(context.getAuthentication()).thenReturn(auth);
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);

            User user = new User();
            user.setUsername("user1");
            user.setRole(Role.LENDER);

            User owner = new User();
            owner.setId(1L);
            owner.setUsername("user1");

            Book savedBook = new Book();
            savedBook.setTitle("New Book");
            savedBook.setOwner(owner);

            when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
            when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
            when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

            BookDto dto = new BookDto();
            dto.setTitle("New Book");
            dto.setAuthor("Author");
            dto.setDescription("A book");
            dto.setOwnerId(1L);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("New Book"));
        }
    }

    @Test
    void shouldReturnForbiddenWhenUserIsNotLender() throws Exception {
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn("user1");

            SecurityContext context = mock(SecurityContext.class);
            when(context.getAuthentication()).thenReturn(auth);
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);

            User user = new User();
            user.setUsername("user1");
            user.setRole(Role.BORROWER);

            when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

            BookDto dto = new BookDto();
            dto.setTitle("Test Book");
            dto.setAuthor("A");
            dto.setDescription("B");
            dto.setOwnerId(1L);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void shouldReturnBadRequestWhenOwnerIdIsMissing() throws Exception {
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn("user1");

            SecurityContext context = mock(SecurityContext.class);
            when(context.getAuthentication()).thenReturn(auth);
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);

            User user = new User();
            user.setUsername("user1");
            user.setRole(Role.LENDER);

            when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

            BookDto dto = new BookDto();
            dto.setTitle("Title");
            dto.setAuthor("Author");
            dto.setDescription("Description");
            dto.setOwnerId(null);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }
}
