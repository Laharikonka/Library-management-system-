package com.library.service;

import com.library.config.DatabaseConnection;
import com.library.dao.FineDAO;
import com.library.model.Fine;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class FineService {
    private FineService() {}
    public static BigDecimal calculate(LocalDate due, LocalDate returned, int perDay) {
        long late = ChronoUnit.DAYS.between(due, returned);
        return late > 0 ? BigDecimal.valueOf(late * perDay) : BigDecimal.ZERO;
    }
    public static BigDecimal calculate(LocalDate due, LocalDate returned) {
        return calculate(due, returned, DatabaseConnection.intProp("fine.per.day"));
    }
    public static List<Fine> unpaid(int studentId) throws SQLException { return FineDAO.unpaid(studentId); }
    public static BigDecimal totalUnpaid(int studentId) throws SQLException { return FineDAO.totalUnpaid(studentId); }
}
