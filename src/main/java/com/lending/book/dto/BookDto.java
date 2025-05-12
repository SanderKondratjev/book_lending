package com.lending.book.dto;

import lombok.Data;

@Data
public class BookDto {
    private String title;
    private String author;
    private String description;
    private Long ownerId;
}
