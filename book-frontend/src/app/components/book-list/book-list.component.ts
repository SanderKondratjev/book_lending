import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common'
import { BookService, Book } from '../../services/book.service';
import { ReservationRequest } from '../../services/book.service';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgFor],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  userId = 1; // Temporary until login is implemented

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.bookService.getBooks().subscribe({
      next: (data: Book[]) => this.books = data,
      error: (err: any) => console.error('Failed to fetch books', err)
    });
  }

  reserve(bookId: number): void {
    const request: ReservationRequest = {
      bookId: bookId,
      userId: this.userId,
      endDate: null
    };

    this.bookService.reserveBook(request).subscribe({
      next: () => alert('Reservation successful'),
      error: err => alert('Reservation failed')
    });
  }
}
