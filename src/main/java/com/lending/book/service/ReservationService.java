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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Reservation createReservation(ReservationDto dto) throws AccessDeniedException {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != Role.BORROWER && user.getRole() != Role.BOTH) {
            throw new AccessDeniedException("You are not authorized to make a reservation");
        }

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        LocalDateTime start = dto.getStartDate() != null ? dto.getStartDate() : LocalDateTime.now();
        LocalDateTime end = dto.getEndDate() != null ? dto.getEndDate() : start.plusWeeks(4);

        Reservation reservation = Reservation.builder()
                .book(book)
                .user(user)
                .status(ReservationStatus.PENDING)
                .startDate(start)
                .endDate(end)
                .build();

        return reservationRepository.save(reservation);
    }
}

