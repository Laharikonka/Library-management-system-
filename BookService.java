package com.library.service;

import com.library.dao.*;
import com.library.model.*;
import java.sql.*;
import java.util.List;

public final class BookService {
    private BookService() {}
    public static int add(String title, String author, int categoryId, int copies) throws SQLException {
        if (copies < 1) throw new IllegalArgumentException("Copies must be at least 1");
        return BookDAO.add(title, author, categoryId, copies);
    }
    public static void delete(int id) throws SQLException {
        try {
            if (!BookDAO.delete(id)) throw new IllegalArgumentException("Book not found");
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException("This book has loan history and cannot be deleted");
        }
    }
    public static List<Book> search(String kw) throws SQLException { return BookDAO.search(kw); }
    public static List<Book> all() throws SQLException { return BookDAO.listAll(); }
    public static void addCategory(String name) throws SQLException { CategoryDAO.add(name); }
    public static List<Category> categories() throws SQLException { return CategoryDAO.listAll(); }
}
