CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

CREATE TABLE books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(150) NOT NULL,
  author VARCHAR(100) NOT NULL,
  copies INT NOT NULL,
  available INT NOT NULL
);

CREATE TABLE members (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) UNIQUE NOT NULL
);

CREATE TABLE issues (
  id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  member_id INT NOT NULL,
  issue_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE NULL,
  fine DECIMAL(8,2) NOT NULL DEFAULT 0,
  fine_paid BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (book_id) REFERENCES books(id),
  FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE payments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  issue_id INT NOT NULL,
  amount DECIMAL(8,2) NOT NULL,
  method ENUM('CASH','CARD','UPI') NOT NULL,
  txn_ref VARCHAR(40) NOT NULL,
  paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (issue_id) REFERENCES issues(id)
);

INSERT INTO books(title,author,copies,available) VALUES
('The Pragmatic Programmer','Andrew Hunt',3,3),
('Clean Code','Robert C. Martin',2,2),
('Wings of Fire','A.P.J. Abdul Kalam',4,4),
('Sapiens','Yuval Noah Harari',2,2);
INSERT INTO members(name,email) VALUES ('Asha Rao','asha@example.com'),('Ravi Kumar','ravi@example.com');
