import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { BookService, Book } from '../../services/book.service';
import { ReservationRequest } from '../../services/book.service';
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgFor, FormsModule],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  userId = 1;
  searchTitle: string = '';

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');

    if (token) {
      this.bookService.getBooks(token, this.searchTitle).subscribe({
        next: (data: Book[]) => this.books = data,
        error: (err: any) => console.error('Failed to fetch books', err)
      });
    } else {
      alert('You are not authenticated.');
    }
  }

  getBooks(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.bookService.getBooks(token, this.searchTitle).subscribe({
        next: (data: Book[]) => this.books = data,
        error: (err: any) => console.error('Failed to fetch books', err)
      });
    } else {
      alert('You are not authenticated.');
    }
  }


  searchBooks(): void {
    this.getBooks();
  }

  reserve(bookId: number): void {
    const token = localStorage.getItem('jwt');
    const request: ReservationRequest = {
      bookId: bookId,
      userId: this.userId,
      endDate: null
    };

    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };

    this.bookService.reserveBook(request, { headers }).subscribe({
      next: () => alert('Reservation successful'),
      error: err => alert('Reservation failed: ' + err.message)
    });
  }
}
