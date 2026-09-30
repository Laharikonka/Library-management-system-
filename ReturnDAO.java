package com.library.dao;

import com.library.config.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public final class ReturnDAO {
    private ReturnDAO() {}
    public static int insert(Connection c, int issueId, LocalDate on, BigDecimal fine) throws SQLException {
        return DatabaseConnection.insert(c, "INSERT INTO returns(issue_id,returned_on,fine_amount) VALUES(?,?,?)", issueId, on, fine);
    }
}
