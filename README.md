# Library Management System (Java + JDBC + MySQL)

Console application with a layered design: `ui -> service -> dao -> MySQL`.

## Features
- Student and Librarian roles, PBKDF2 password hashing, session handling
- Books and categories: add, search, delete, stock tracking
- Issue and return with transactions, loan limit and due dates
- Automatic fine on late returns, fine payment by Cash, Card or UPI
- Text receipts saved to `receipts/`, in-app notifications, reports
- JUnit 5 tests for the logic that needs no database

## Setup
1. Run the SQL files in this order:
   ```
   mysql -u root -p < database/library_db.sql
   mysql -u root -p < database/database_schema.sql
   mysql -u root -p < database/sample_data.sql
   ```
2. Edit `src/main/resources/application.properties` with your MySQL user and password. Do not commit a real password.
3. Run: `mvn compile exec:java`
4. First login: `admin@library.com` / `admin123` (created automatically on first start; change it). Students can sign up from the login menu.
5. Tests: `mvn test`

## Card and UPI
Card numbers and UPI ids are only validated, never stored. Connect a real gateway inside `PaymentService.pay` for live payments.
