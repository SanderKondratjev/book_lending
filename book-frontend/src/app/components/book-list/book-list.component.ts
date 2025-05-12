import { Component, OnInit } from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import { BookService, Book } from '../../services/book.service';
import { ReservationRequest } from '../../services/book.service';
import { FormsModule } from "@angular/forms";
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgFor, FormsModule, NgIf],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  userId = 1;
  searchTitle: string = '';
  isLender: boolean = false;

  constructor(private bookService: BookService, private authService: AuthService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');

    if (token) {
      this.isLender = this.authService.isLenderOrBoth();
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
    if (this.isLender) {
      alert("Lenders cannot reserve books.");
      return;
    }

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
