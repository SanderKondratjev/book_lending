import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddBookComponent } from './add-book.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../services/auth.service';
import { BookService } from '../../services/book.service';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';

describe('AddBookComponent', () => {
  let component: AddBookComponent;
  let fixture: ComponentFixture<AddBookComponent>;
  let bookService: jasmine.SpyObj<BookService>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    bookService = jasmine.createSpyObj('BookService', ['addBook']);
    authService = jasmine.createSpyObj('AuthService', ['isLenderOrBoth']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [AddBookComponent],
      providers: [
        { provide: BookService, useValue: bookService },
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(AddBookComponent);
    component = fixture.componentInstance;
  });

  it('should create the AddBookComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should call addBook and navigate to books list on success', async () => {
    // Setup mock response
    const mockBook = { id: 1, title: 'Test Book', author: 'Test Author', description: 'Test Description' };
    bookService.addBook.and.returnValue(of(mockBook));
    authService.isLenderOrBoth.and.returnValue(true);

    component.title = 'Test Book';
    component.author = 'Test Author';
    component.description = 'Test Description';

    await component.addBook();

    expect(bookService.addBook).toHaveBeenCalledWith(
      { title: 'Test Book', author: 'Test Author', description: 'Test Description', ownerId: 1 },
      { headers: jasmine.any(Object) }
    );
    expect(router.navigate).toHaveBeenCalledWith(['/books']);
  });

  it('should display error message when adding book fails', async () => {
    const errorMessage = 'Failed to add book';

    bookService.addBook.and.returnValue(throwError({ message: errorMessage }));
    authService.isLenderOrBoth.and.returnValue(true); // Mock role as Lender or Both

    component.title = 'Test Book';
    component.author = 'Test Author';
    component.description = 'Test Description';

    await component.addBook();

    expect(component.errorMessage).toBe('Failed to add book: ' + errorMessage);
  });

  it('should not call addBook if user is not Lender or Both', async () => {
    authService.isLenderOrBoth.and.returnValue(false); // Mock role as something other than Lender or Both

    component.title = 'Test Book';
    component.author = 'Test Author';
    component.description = 'Test Description';

    await component.addBook();

    expect(bookService.addBook).not.toHaveBeenCalled();
  });
});
