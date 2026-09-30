package com.library.dao;

import static com.library.config.DatabaseConnection.query;
import com.library.config.DatabaseConnection;
import com.library.model.Payment;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public final class PaymentDAO {
    private PaymentDAO() {}
    static Payment map(ResultSet rs) throws SQLException {
        return new Payment(rs.getInt("id"), rs.getInt("fine_id"), rs.getBigDecimal("amount"), rs.getString("method"),
            rs.getString("txn_ref"), rs.getObject("paid_at", LocalDateTime.class));
    }
    public static int insert(Connection c, int fineId, BigDecimal amount, String method, String ref) throws SQLException {
        return DatabaseConnection.insert(c, "INSERT INTO payments(fine_id,amount,method,txn_ref) VALUES(?,?,?,?)", fineId, amount, method, ref);
    }
    public static List<Payment> byStudent(int studentId) throws SQLException {
        return query("SELECT p.* FROM payments p JOIN fines f ON f.id=p.fine_id WHERE f.student_id=? ORDER BY p.id DESC", PaymentDAO::map, studentId);
    }
    public static List<Payment> all() throws SQLException { return query("SELECT * FROM payments ORDER BY id DESC", PaymentDAO::map); }
    public static BigDecimal totalCollected() throws SQLException {
        return query("SELECT IFNULL(SUM(amount),0) FROM payments", rs -> rs.getBigDecimal(1)).get(0);
    }
}
