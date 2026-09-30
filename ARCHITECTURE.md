# Architecture
- **ui**: console menus, read input and print output only
- **service**: business rules (loan limits, fines, payments), owns transactions
- **dao**: SQL through JDBC PreparedStatements
- **config**: connection and settings from application.properties
- **security**: PBKDF2 hashing, authentication, session
- **model**: plain data classes and records
- **util**: validation, dates, receipts

Tables: users, students, librarians, categories, books, issues, returns, fines, payments, notifications.

Add your ER diagram, architecture image, report and screenshots to this folder.
