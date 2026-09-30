package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.*;
import java.sql.*;
import java.util.*;

public final class StudentDAO {
    private StudentDAO() {}
    private static final String SELECT = "SELECT u.*, s.roll_no, s.department FROM users u JOIN students s ON s.user_id=u.id";
    static Student map(ResultSet rs) throws SQLException {
        return new Student(UserDAO.map(rs), rs.getString("roll_no"), rs.getString("department"));
    }
    public static int register(String name, String email, String hash, String roll, String dept) throws SQLException {
        return tx(c -> {
            int id = UserDAO.insert(c, name, email, hash, "STUDENT");
            update(c, "INSERT INTO students(user_id,roll_no,department) VALUES(?,?,?)", id, roll, dept);
            return id;
        });
    }
    public static Optional<Student> findByUserId(int id) throws SQLException {
        return query(SELECT + " WHERE u.id=?", StudentDAO::map, id).stream().findFirst();
    }
    public static List<Student> listAll() throws SQLException { return query(SELECT + " ORDER BY u.name", StudentDAO::map); }
}
