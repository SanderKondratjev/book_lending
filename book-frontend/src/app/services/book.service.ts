import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Book {
  id: number;
  title: string;
  author: string;
  description: string;
}

export interface ReservationRequest {
  bookId: number;
  userId: number;
  endDate?: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private baseUrl = '/api/books';

  constructor(private http: HttpClient) {}

  getBooks(token: string): Observable<Book[]> {
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };

    return this.http.get<Book[]>(this.baseUrl, { headers });
  }

  reserveBook(reservation: ReservationRequest, options: { headers: any }): Observable<any> {
    return this.http.post('/api/reservations', reservation, options);
  }
}
