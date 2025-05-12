package com.lending.book.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long bookId;
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
