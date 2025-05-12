--sander
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    description TEXT,
    owner_id BIGINT,
    available BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_books_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);
