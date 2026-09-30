package com.library.service;

import com.library.dao.LibrarianDAO;
import com.library.model.Librarian;
import java.sql.SQLException;
import java.util.List;

public final class LibrarianService {
    private LibrarianService() {}
    public static int add(String name, String email, String password, String employeeId) throws SQLException {
        return LoginService.registerLibrarian(name, email, password, employeeId);
    }
    public static List<Librarian> all() throws SQLException { return LibrarianDAO.listAll(); }
}
