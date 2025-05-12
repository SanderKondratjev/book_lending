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
  userId = 1;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private bookService: BookService,
    private router: Router
  ) {}

  async addBook(): Promise<void> {
    if (!this.canAddBook()) {
      return;
    }

    const token = this.getToken();
    if (!token) {
      this.handleError('No authentication token found.');
      return;
    }

    const bookRequest = this.createBookRequest();
    await this.sendBookRequest(bookRequest, token);
  }

  private createBookRequest(): any {
    return {
      title: this.title,
      author: this.author,
      description: this.description,
      ownerId: this.userId
    };
  }

  private async sendBookRequest(bookRequest: any, token: string): Promise<void> {
    const headers = this.createHeaders(token);

    try {
      const book = await this.bookService.addBook(bookRequest, { headers }).toPromise();
      this.handleSuccess(book);
    } catch (err) {
      this.handleError(err);
    }
  }

  private createHeaders(token: string): any {
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
  }

  private getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  private handleSuccess(book: any): void {
    console.log('Book added:', book);
    this.router.navigate(['/books']).then(() => {
      console.log('Navigation successful');
    }).catch((error) => {
      console.error('Navigation failed:', error);
    });
  }

  private handleError(err: any): void {
    this.errorMessage = `Failed to add book: ${err.message}`;
    console.error('Add book error:', err);
  }

  canAddBook(): boolean {
    return this.authService.isLenderOrBoth();
  }
}
