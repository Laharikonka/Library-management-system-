package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Fine;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public final class FineDAO {
    private FineDAO() {}
    static Fine map(ResultSet rs) throws SQLException {
        return new Fine(rs.getInt("id"), rs.getInt("issue_id"), rs.getInt("student_id"), rs.getBigDecimal("amount"), rs.getBoolean("paid"));
    }
    public static int create(Connection c, int issueId, int studentId, BigDecimal amount) throws SQLException {
        return insert(c, "INSERT INTO fines(issue_id,student_id,amount) VALUES(?,?,?)", issueId, studentId, amount);
    }
    public static Optional<Fine> findById(Connection c, int id) throws SQLException {
        return query(c, "SELECT * FROM fines WHERE id=? FOR UPDATE", FineDAO::map, id).stream().findFirst();
    }
    public static List<Fine> unpaid(int studentId) throws SQLException {
        return query("SELECT * FROM fines WHERE student_id=? AND paid=0 ORDER BY id", FineDAO::map, studentId);
    }
    public static BigDecimal totalUnpaid(int studentId) throws SQLException {
        return query("SELECT IFNULL(SUM(amount),0) FROM fines WHERE student_id=? AND paid=0", rs -> rs.getBigDecimal(1), studentId).get(0);
    }
    public static void markPaid(Connection c, int id) throws SQLException { update(c, "UPDATE fines SET paid=1 WHERE id=?", id); }
}
