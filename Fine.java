package com.library.model;

import java.math.BigDecimal;

public record Fine(int id, int issueId, int studentId, BigDecimal amount, boolean paid) {}
