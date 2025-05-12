import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { BookService } from '../../services/book.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent {
  title = '';
  author = '';
  description = '';
  userId = 1; // Make sure this is set properly (or dynamic)
  errorMessage = '';

  constructor(public authService: AuthService, private bookService: BookService, private router: Router) {}

  addBook(): void {
    const token = localStorage.getItem('jwt');

    // Create the request body
    const request = {
      title: this.title,
      author: this.author,
      description: this.description,
      ownerId: this.userId
    };

    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };

    this.bookService.addBook(request, { headers }).subscribe({
      next: (book) => {
        console.log('Book added:', book);
        this.router.navigate(['/books']);
      },
      error: (err) => {
        this.errorMessage = 'Failed to add book: ' + err.message;
        console.error('Add book error:', err);
      }
    });
  }

  canAddBook(): boolean {
    return this.authService.isLenderOrBoth();
  }
}
