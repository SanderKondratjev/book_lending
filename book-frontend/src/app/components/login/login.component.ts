import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  username = '';
  password = '';
  email = '';
  role = 'BORROWER';
  errorMessage = '';
  isRegistering = false;

  constructor(private http: HttpClient, private router: Router) {}

  toggleRegister(): void {
    this.isRegistering = !this.isRegistering;
  }

  login(): void {
    const body = {
      username: this.username,
      password: this.password
    };

    this.http.post('/api/auth/login', body, { responseType: 'text' }).subscribe({
      next: (token: string) => {
        localStorage.setItem('jwt', token);
        this.router.navigate(['/books']);
      },
      error: (err) => {
        this.errorMessage = 'Login failed';
        console.error(err);
      }
    });
  }

  register(): void {
    const body = {
      username: this.username,
      password: this.password,
      email: this.email,
      role: this.role
    };

    this.http.post('/api/users/register', body).subscribe({
      next: (user) => {
        console.log('User registered:', user);
        this.isRegistering = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = 'Registration failed';
        console.error(err);
      }
    });
  }
}
