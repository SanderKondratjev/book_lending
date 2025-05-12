import { Component } from '@angular/core';
import { BookListComponent } from './components/book-list/book-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [BookListComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'book-frontend';
}
