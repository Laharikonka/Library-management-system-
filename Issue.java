package com.library.model;

import java.time.LocalDate;

public record Issue(int id, int bookId, int studentId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {}
