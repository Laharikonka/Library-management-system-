package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Issue;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public final class IssueDAO {
    private IssueDAO() {}
    static Issue map(ResultSet rs) throws SQLException {
        return new Issue(rs.getInt("id"), rs.getInt("book_id"), rs.getInt("student_id"),
            rs.getObject("issue_date", LocalDate.class), rs.getObject("due_date", LocalDate.class), rs.getObject("return_date", LocalDate.class));
    }
    public static int create(Connection c, int bookId, int studentId, LocalDate issued, LocalDate due) throws SQLException {
        return insert(c, "INSERT INTO issues(book_id,student_id,issue_date,due_date) VALUES(?,?,?,?)", bookId, studentId, issued, due);
    }
    public static Optional<Issue> findById(Connection c, int id) throws SQLException {
        return query(c, "SELECT * FROM issues WHERE id=? FOR UPDATE", IssueDAO::map, id).stream().findFirst();
    }
    public static int countActive(Connection c, int studentId) throws SQLException {
        return query(c, "SELECT COUNT(*) FROM issues WHERE student_id=? AND return_date IS NULL", rs -> rs.getInt(1), studentId).get(0);
    }
    public static void markReturned(Connection c, int id, LocalDate date) throws SQLException {
        update(c, "UPDATE issues SET return_date=? WHERE id=?", date, id);
    }
    /** Ready-to-print rows of open loans. studentId 0 = everyone. */
    public static List<String> view(int studentId, boolean overdueOnly) throws SQLException {
        String sql = "SELECT i.id, b.title, u.name, i.due_date, i.due_date<CURDATE() late FROM issues i"
            + " JOIN books b ON b.id=i.book_id JOIN users u ON u.id=i.student_id WHERE i.return_date IS NULL"
            + (studentId > 0 ? " AND i.student_id=" + studentId : "")
            + (overdueOnly ? " AND i.due_date<CURDATE()" : "") + " ORDER BY i.due_date";
        return query(sql, rs -> String.format("Loan #%d | %s | %s | due %s%s", rs.getInt(1), rs.getString(2),
            rs.getString(3), rs.getString(4), rs.getBoolean(5) ? " | OVERDUE" : ""));
    }
}
