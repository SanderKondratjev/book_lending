import { Injectable } from '@angular/core';
import { CanActivate, UrlTree, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('jwt');
    if (token) {
      return true;
    }
    return this.router.createUrlTree(['/login']);
  }
}
