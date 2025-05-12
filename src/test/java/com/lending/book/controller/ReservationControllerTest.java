package com.lending.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lending.book.dto.ReservationDto;
import com.lending.book.entity.Reservation;
import com.lending.book.enums.ReservationStatus;
import com.lending.book.exception.GlobalExceptionHandler;
import com.lending.book.repository.ReservationRepository;
import com.lending.book.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationControllerTest {

    private MockMvc mockMvc;
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationService = mock(ReservationService.class);
        ReservationController controller = new ReservationController(reservationRepository, reservationService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        ReservationDto dto = new ReservationDto();
        dto.setBookId(1L);
        dto.setUserId(2L);
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusWeeks(4));

        Reservation reservation = new Reservation();
        reservation.setId(100L);

        when(reservationService.createReservation(any(ReservationDto.class))).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void shouldReturnForbiddenWhenReservationNotAllowed() throws Exception {
        ReservationDto dto = new ReservationDto();
        dto.setBookId(1L);
        dto.setUserId(2L);
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusWeeks(4));

        when(reservationService.createReservation(any(ReservationDto.class)))
                .thenThrow(new AccessDeniedException("Not allowed"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCancelReservationSuccessfully() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(patch("/api/reservations/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMarkReservationAsReceived() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(patch("/api/reservations/1/receive"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMarkReservationAsReturned() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.RECEIVED);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(patch("/api/reservations/1/return"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenReservationDoesNotExist() throws Exception {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/reservations/999/cancel"))
                .andExpect(status().isNotFound());
    }
}
