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

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.baseUrl);
  }

  reserveBook(reservation: ReservationRequest): Observable<any> {
    return this.http.post('/api/reservations', reservation);
  }
}
