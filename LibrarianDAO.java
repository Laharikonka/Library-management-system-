package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Librarian;
import java.sql.*;
import java.util.*;

public final class LibrarianDAO {
    private LibrarianDAO() {}
    public static int register(String name, String email, String hash, String employeeId) throws SQLException {
        return tx(c -> {
            int id = UserDAO.insert(c, name, email, hash, "LIBRARIAN");
            update(c, "INSERT INTO librarians(user_id,employee_id) VALUES(?,?)", id, employeeId);
            return id;
        });
    }
    public static int count() throws SQLException { return query("SELECT COUNT(*) FROM librarians", rs -> rs.getInt(1)).get(0); }
    public static List<Librarian> listAll() throws SQLException {
        return query("SELECT u.*, l.employee_id FROM users u JOIN librarians l ON l.user_id=u.id ORDER BY u.name",
            rs -> new Librarian(UserDAO.map(rs), rs.getString("employee_id")));
    }
}
