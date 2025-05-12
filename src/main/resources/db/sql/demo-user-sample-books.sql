--sander
INSERT INTO users (username, password_hash, email, role)
VALUES ('demo_user', 'demo_pass', 'demo@mail.com', 'BORROWER');

INSERT INTO books (title, author, description, owner_id)
VALUES
    ('The Pragmatic Programmer', 'Andrew Hunt', 'Tips for modern software development.', 1),
    ('Clean Architecture', 'Robert C. Martin', 'Software design principles.', 1),
    ('Domain-Driven Design', 'Eric Evans', 'Modeling complex systems.', 1),
    ('Refactoring', 'Martin Fowler', 'Improve the design of existing code.', 1),
    ('Effective Java', 'Joshua Bloch', 'Best practices for Java programming.', 1),
    ('Spring in Action', 'Craig Walls', 'Spring framework explained.', 1),
    ('You Don’t Know JS', 'Kyle Simpson', 'JavaScript deep dive series.', 1),
    ('The Clean Coder', 'Robert C. Martin', 'Professionalism in software development.', 1),
    ('Introduction to Algorithms', 'Cormen et al.', 'Classic algorithms textbook.', 1),
    ('Design Patterns', 'Gamma et al.', 'Reusable object-oriented solutions.', 1);
