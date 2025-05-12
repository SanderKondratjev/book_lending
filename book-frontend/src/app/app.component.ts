import { Component } from '@angular/core';
import { BookListComponent } from './components/book-list/book-list.component';
import {RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-root',
  standalone: true,
    imports: [BookListComponent, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'book-frontend';
}
