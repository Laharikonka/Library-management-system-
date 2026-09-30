package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Payment(int id, int fineId, BigDecimal amount, String method, String txnRef, LocalDateTime paidAt) {}
