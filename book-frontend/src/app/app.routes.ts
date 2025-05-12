import { Routes } from '@angular/router';
import { AddBookComponent } from './components/add-book/add-book.component';
import { BookListComponent } from './components/book-list/book-list.component';
import { LoginComponent } from './components/login/login.component';
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
    path: 'add-book',
    component: AddBookComponent,
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
