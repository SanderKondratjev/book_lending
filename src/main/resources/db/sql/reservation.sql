--sander
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id)
);
