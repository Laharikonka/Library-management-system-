package com.library.service;

import static com.library.config.DatabaseConnection.query;
import com.library.dao.*;
import java.sql.SQLException;
import java.util.*;

public final class ReportService {
    private ReportService() {}
    private static int n(String sql) throws SQLException { return query(sql, rs -> rs.getInt(1)).get(0); }

    public static List<String> summary() throws SQLException {
        return List.of(
            "Book titles         : " + n("SELECT COUNT(*) FROM books"),
            "Copies on shelf     : " + n("SELECT IFNULL(SUM(available_copies),0) FROM books"),
            "Books on loan       : " + n("SELECT COUNT(*) FROM issues WHERE return_date IS NULL"),
            "Overdue loans       : " + n("SELECT COUNT(*) FROM issues WHERE return_date IS NULL AND due_date<CURDATE()"),
            "Students            : " + n("SELECT COUNT(*) FROM students"),
            "Unpaid fines (Rs)   : " + query("SELECT IFNULL(SUM(amount),0) FROM fines WHERE paid=0", rs -> rs.getBigDecimal(1)).get(0),
            "Fines collected (Rs): " + PaymentDAO.totalCollected());
    }
    public static List<String> overdue() throws SQLException { return IssueDAO.view(0, true); }
}
