import { Component } from '@angular/core';
import { RouterOutlet } from "@angular/router";
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgIf],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'book-frontend';

  constructor(public authService: AuthService, private router: Router) {}

  logout(): void {
    this.authService.logout();
  }

  navigateToAddBook(): void {
    this.router.navigate(['/add-book']);
  }
}
