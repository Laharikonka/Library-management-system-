package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReturnBook(int id, int issueId, LocalDate returnedOn, BigDecimal fineAmount) {}
