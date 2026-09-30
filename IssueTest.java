package com.library;

import static org.junit.jupiter.api.Assertions.*;
import com.library.service.FineService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IssueTest {
    private final LocalDate due = LocalDate.of(2026, 1, 15);
    @Test void noFineWhenOnTime() { assertEquals(BigDecimal.ZERO, FineService.calculate(due, due, 5)); }
    @Test void noFineWhenEarly() { assertEquals(BigDecimal.ZERO, FineService.calculate(due, due.minusDays(2), 5)); }
    @Test void finesPerLateDay() { assertEquals(BigDecimal.valueOf(15), FineService.calculate(due, due.plusDays(3), 5)); }
}
