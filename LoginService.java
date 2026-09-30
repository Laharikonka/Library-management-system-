package com.library.service;

import com.library.dao.*;
import com.library.model.User;
import com.library.security.*;
import com.library.util.InputValidator;
import java.sql.SQLException;

public final class LoginService {
    private LoginService() {}

    public static User login(String email, String password) throws SQLException {
        User u = Authentication.authenticate(email, password).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        SessionManager.login(u);
        return u;
    }
    public static void logout() { SessionManager.logout(); }

    private static void check(String email, String password) throws SQLException {
        if (!InputValidator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email");
        if (password.length() < 6) throw new IllegalArgumentException("Password needs at least 6 characters");
        if (UserDAO.emailExists(email)) throw new IllegalArgumentException("Email already registered");
    }
    public static int registerStudent(String name, String email, String password, String roll, String dept) throws SQLException {
        check(email, password);
        return StudentDAO.register(name, email, PasswordUtil.hash(password), roll, dept);
    }
    public static int registerLibrarian(String name, String email, String password, String employeeId) throws SQLException {
        check(email, password);
        return LibrarianDAO.register(name, email, PasswordUtil.hash(password), employeeId);
    }
    /** Creates a first librarian so someone can log in on a fresh database. */
    public static boolean ensureDefaultAdmin() throws SQLException {
        if (LibrarianDAO.count() > 0) return false;
        registerLibrarian("Admin", "admin@library.com", "admin123", "LIB001");
        return true;
    }
}
