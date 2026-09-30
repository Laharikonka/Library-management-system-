package com.library.dao;

import static com.library.config.DatabaseConnection.query;
import com.library.model.User;
import java.sql.*;
import java.util.Optional;

public final class UserDAO {
    private UserDAO() {}
    static User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role"), rs.getString("password_hash"));
    }
    public static Optional<User> findByEmail(String email) throws SQLException {
        return query("SELECT * FROM users WHERE email=?", UserDAO::map, email).stream().findFirst();
    }
    public static Optional<User> findById(int id) throws SQLException {
        return query("SELECT * FROM users WHERE id=?", UserDAO::map, id).stream().findFirst();
    }
    public static boolean emailExists(String email) throws SQLException { return findByEmail(email).isPresent(); }
    public static int insert(Connection c, String name, String email, String hash, String role) throws SQLException {
        return com.library.config.DatabaseConnection.insert(c, "INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)", name, email, hash, role);
    }
}
