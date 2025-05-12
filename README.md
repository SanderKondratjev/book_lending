# 📚 Book Lending Application

A crowdsourced book lending system built with Java, Spring Boot, Angular, and PostgreSQL — created as part of the **Isikutuvastus proovitöö** developer test.

---

## ✅ Features Implemented (Backend Only)

### 👤 User Management

- Register a new user (`BORROWER`, `LENDER`, `BOTH`)
- Secure login using JWT authentication

### 📚 Book Lending (LENDER or BOTH)

- Add books for lending
- Search books by title or list all

### 📖 Book Borrowing (BORROWER or BOTH)

- Reserve a book
- Cancel a reservation
- Mark a reservation as received
- Mark a reservation as returned

---

## 🚫 Features Not Yet Implemented

These features were not implemented due to time constraints:

- Cancel reservation by the book **owner**
- Mark book as **handed over** by the **owner**
- Remove a book from lending
- Owner canceling a reservation
- Marking book as received or returned from **owner's** perspective
- Full frontend for all operations

---

## 🧪 Quality & Requirements

| Requirement                       | Status   |
|-----------------------------------|----------|
| All actions are logged            | ✅ Done  |
| Error messages shown to users     | ✅ Done  |
| REST endpoints protected with JWT | ✅ Done  |
| Swagger/OpenAPI documentation     | ✅ Done  |
| Code covered partially with tests | ✅ Done  |
| Dockerized with PostgreSQL        | ✅ Done  |
| Instructions included in README   | ✅ Done  |

---

## 🧰 Tech Stack

| Layer     | Stack                              |
|-----------|-------------------------------------|
| Backend   | Java 17, Spring Boot 3.4.5          |
| Frontend  | Angular                             |
| Database  | PostgreSQL                          |
| Docs      | Swagger (`springdoc-openapi 2.8.6`) |
| Auth      | Spring Security + JWT               |
| Runtime   | Docker                              |
| Tests     | JUnit 5, Mockito, MockMvc           |

---

## 🚀 How to Run the Project

### 📦 Prerequisites

- Docker & Docker Compose installed
- Java 17+
- Node.js + npm (for frontend)

---

### 🐳 Backend (Spring Boot)

```bash
# 1. Clone the repository
git clone https://github.com/SanderKondratjev/book_lending
cd book

# 2. Start PostgreSQL via Docker
docker compose up -d

# 3. Run the backend
./gradlew bootRun
```

The backend will run at: http://localhost:8080

---
### 📖 API Documentation
Once the backend is running, Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

And the OpenAPI spec is served at:

http://localhost:8080/v3/api-docs

---
### 🌐 Frontend (Angular)
1. Navigate to the frontend directory
cd book-frontend

2. Install dependencies
npm install

3. Run the app
ng serve

The frontend will run at: http://localhost:4200

---
