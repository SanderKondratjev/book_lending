package com.lending.book.service;

import com.lending.book.dto.ReservationDto;
import com.lending.book.entity.Book;
import com.lending.book.entity.Reservation;
import com.lending.book.entity.User;
import com.lending.book.enums.ReservationStatus;
import com.lending.book.enums.Role;
import com.lending.book.repository.BookRepository;
import com.lending.book.repository.ReservationRepository;
import com.lending.book.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        reservationService = new ReservationService(reservationRepository, userRepository, bookRepository);
    }

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        ReservationDto dto = new ReservationDto();
        dto.setUserId(1L);
        dto.setBookId(2L);
        dto.setStartDate(LocalDateTime.of(2025, 1, 1, 10, 0));
        dto.setEndDate(LocalDateTime.of(2025, 2, 1, 10, 0));

        User user = new User();
        user.setId(1L);
        user.setRole(Role.BORROWER);

        Book book = new Book();
        book.setId(2L);

        Reservation savedReservation = Reservation.builder()
                .id(99L)
                .user(user)
                .book(book)
                .status(ReservationStatus.PENDING)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        Reservation result = reservationService.createReservation(dto);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(book, result.getBook());
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        assertEquals(dto.getStartDate(), result.getStartDate());
        assertEquals(dto.getEndDate(), result.getEndDate());
    }

    @Test
    void shouldThrowAccessDeniedWhenUserIsNotBorrowerOrBoth() {
        ReservationDto dto = new ReservationDto();
        dto.setUserId(1L);
        dto.setBookId(2L);

        User user = new User();
        user.setId(1L);
        user.setRole(Role.LENDER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> reservationService.createReservation(dto));
    }

    @Test
    void shouldThrowEntityNotFoundWhenUserDoesNotExist() {
        ReservationDto dto = new ReservationDto();
        dto.setUserId(1L);
        dto.setBookId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(dto));
    }

    @Test
    void shouldThrowEntityNotFoundWhenBookDoesNotExist() {
        ReservationDto dto = new ReservationDto();
        dto.setUserId(1L);
        dto.setBookId(2L);

        User user = new User();
        user.setId(1L);
        user.setRole(Role.BORROWER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(dto));
    }
}
