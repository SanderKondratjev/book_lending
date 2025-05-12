package com.lending.book.controller;

import com.lending.book.dto.ReservationDto;
import com.lending.book.entity.Reservation;
import com.lending.book.enums.ReservationStatus;
import com.lending.book.repository.ReservationRepository;
import com.lending.book.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Endpoints for reserving and managing book reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a reservation")
    public ResponseEntity<Reservation> reserveBook(@RequestBody ReservationDto dto) {
        try {
            Reservation reservation = reservationService.createReservation(dto);
            return ResponseEntity.ok(reservation);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a reservation")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        return updateStatus(id, ReservationStatus.CANCELLED);
    }

    @PatchMapping("/{id}/receive")
    @Operation(summary = "Mark a reservation as received")
    public ResponseEntity<Void> markAsReceived(@PathVariable Long id) {
        return updateStatus(id, ReservationStatus.RECEIVED);
    }

    @PatchMapping("/{id}/return")
    @Operation(summary = "Mark a reservation as returned")
    public ResponseEntity<Void> markAsReturned(@PathVariable Long id) {
        return updateStatus(id, ReservationStatus.RETURNED);
    }

    private ResponseEntity<Void> updateStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        reservation.setStatus(status);
        reservationRepository.save(reservation);
        return ResponseEntity.ok().build();
    }
}
