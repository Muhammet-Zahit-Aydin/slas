# Library Management System (SLAS)

A full-stack Library Management System built with **Spring Boot** and **Vanilla JavaScript**. This project demonstrates a RESTful architecture, secure JWT authentication, and complex business logic for borrowing/returning books with dynamic stock management.

## Features

* **User Authentication:** Secure Login/Register system using **JWT (JSON Web Token)**.
* **Role-Based Access:** Admin (add/delete books) and User (borrow/return books) roles.
* **Book Management:**
    * CRUD operations for books.
    * **Dynamic Categorization:** Filter books by genre (Fiction, Sci-Fi, History, etc.).
    * **Real-time Search:** Client-side filtering for instant results.
* **Borrowing System:**
    * **Transactional Integrity:** Stock decreases automatically when a book is borrowed.
    * **Validation:** Users cannot borrow if stock is 0.
* **Return & Penalty Logic:**
    * Calculates overdue days automatically using Java Time API.
    * Generates penalty fees for late returns.
    * Restores stock automatically upon return.
* **Architecture:** Layered Architecture (Controller -> Service -> Repository).

## Tech Stack

### Backend
* **Java 17**
* **Spring Boot 3.x** (Web, Data JPA, Security)
* **Hibernate** (ORM)
* **MSSQL (SQL Server)** (Database)
* **JWT (JJE)** (Stateless Authentication)
* **Lombok** (Boilerplate reduction)

### Frontend
* **HTML5 & CSS3** (Responsive Design)
* **Vanilla JavaScript (ES6+)**
* **Fetch API** (Asynchronous HTTP requests)

---

## Installation & Setup

### 1. Database Setup
1.  Make sure **SQL Server** is running.
2.  Create a database named `slasdb`.
3.  The tables will be automatically created by Hibernate (assuming `ddl-auto` is set to `update`).

### 2. Backend Configuration
1.  Open `src/main/resources/application.properties`.
2.  Update your database credentials:
    ```properties
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=slasdb;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=YOUR_DB_USERNAME
    spring.datasource.password=YOUR_DB_PASSWORD
    ```
3.  Run the application using Maven or your IDE.

### 3. Frontend Setup
1.  Navigate to the `frontend` folder (or where your HTML files are).
2.  Open `index.html` in your browser.
3.  *(Optional)* Use "Live Server" extension in VS Code for a better experience.

---

## API Endpoints (Brief)

| Method | Endpoint | Description | Role |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Login & Get JWT Token | Public |
| `GET` | `/api/books` | Get all books | User/Admin |
| `POST` | `/api/books/add` | Add a new book | **Admin** |
| `DELETE` | `/api/books/delete/{id}`| Delete a book | **Admin** |
| `POST` | `/api/borrow/{bookId}` | Borrow a book | User |
| `PUT` | `/api/borrow/return/{id}`| Return a book | User |

---

## Business Logic Highlights

### Borrowing Process (`@Transactional`)
When a user clicks "Kirala":
1.  Backend checks if `Stock > 0`.
2.  Creates a `Borrowing` record with `BorrowDate` (Now) and `ReturnDate` (Now + 15 Days).
3.  Decrements the Book's stock by 1.
4.  If any error occurs, the entire transaction is rolled back.

### Filtering Mechanism
The frontend uses **Client-Side Filtering** to provide a fast user experience. Books are fetched once, and JavaScript filters the array based on:
* Search Input (Title/Author)
* Category Selection (Genre)

---

## Author

Developed by **[Senin Adın]**