# Stacks — Library Management System

Java 17 + JDBC (MySQL) back end, single-page HTML front end. No frameworks.

## Features
- Books and members: add, search, stock tracking
- Shelf view: each book is a colored spine; tap one to lend it
- Loans: 14-day lending, returns, automatic fine of ₹5 per late day
- Fine payment: Cash, Card or UPI, with a unique transaction reference and a payment history
- Dashboard: totals, overdue count, fines collected, and a daily book pick

## Run
1. Create the database: `mysql -u root -p < database/schema.sql`
2. Set credentials if yours differ from root/root:
   `export DB_URL=jdbc:mysql://localhost:3306/librarydb DB_USER=root DB_PASS=yourpass`
3. Start: `mvn compile exec:java`
4. Open http://localhost:8080

## Structure
```
database/schema.sql
pom.xml
src/main/java/library/Db.java       JDBC helpers
src/main/java/library/Server.java   API endpoints + static page
src/main/resources/public/index.html
```
The payment step is a stand-in: swap the `txn_ref` generation in `Server.pay()` for a real gateway call (Razorpay, Stripe) when you go live.
