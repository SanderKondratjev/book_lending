import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { BookListComponent } from './components/book-list/book-list.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'books',
    pathMatch: 'full'
  },
  {
    path: 'books',
    component: BookListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
