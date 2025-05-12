import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router) {}

  logout(): void {
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }

  getRole(): string | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const payload = token.split('.')[1];
      const decoded = this.base64UrlDecode(payload);
      const decodedToken = JSON.parse(decoded);
      return decodedToken.role;
    }
    return null;
  }

  private base64UrlDecode(base64Url: string): string {
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return atob(base64);
  }

  isLenderOrBoth(): boolean {
    const role = this.getRole();
    return role === 'LENDER' || role === 'BOTH';
  }
}
