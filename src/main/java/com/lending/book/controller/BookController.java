package com.lending.book.controller;

import com.lending.book.dto.BookDto;
import com.lending.book.entity.Book;
import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.repository.BookRepository;
import com.lending.book.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Endpoints for searching, adding and deleting books")
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Search books by title or return all")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(value = "title", required = false) String title) {
        List<Book> books = (title == null || title.isBlank())
                ? bookRepository.findAll()
                : bookRepository.findByTitleContainingIgnoreCase(title);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    @Operation(summary = "Add a book to be shared")
    public ResponseEntity<Book> addBook(@RequestBody BookDto bookDto) {
        log.info("Adding book: {}", bookDto.getTitle());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != Role.LENDER && user.getRole() != Role.BOTH) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (bookDto.getOwnerId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        User owner = userRepository.findById(bookDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setDescription(bookDto.getDescription());
        book.setOwner(owner);
        book.setAvailable(true);

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
