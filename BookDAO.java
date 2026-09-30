package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Book;
import java.sql.*;
import java.util.*;

public final class BookDAO {
    private BookDAO() {}
    static Book map(ResultSet rs) throws SQLException {
        return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
            rs.getInt("category_id"), rs.getInt("total_copies"), rs.getInt("available_copies"));
    }
    public static int add(String title, String author, int categoryId, int copies) throws SQLException {
        return insert("INSERT INTO books(title,author,category_id,total_copies,available_copies) VALUES(?,?,?,?,?)",
            title, author, categoryId, copies, copies);
    }
    public static boolean delete(int id) throws SQLException { return update("DELETE FROM books WHERE id=?", id) == 1; }
    public static Optional<Book> findById(int id) throws SQLException {
        return query("SELECT * FROM books WHERE id=?", BookDAO::map, id).stream().findFirst();
    }
    public static List<Book> search(String kw) throws SQLException {
        String like = "%" + kw + "%";
        return query("SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY title", BookDAO::map, like, like);
    }
    public static List<Book> listAll() throws SQLException { return query("SELECT * FROM books ORDER BY title", BookDAO::map); }
    /** Takes one copy off the shelf; false when none is left. */
    public static boolean take(Connection c, int id) throws SQLException {
        return update(c, "UPDATE books SET available_copies=available_copies-1 WHERE id=? AND available_copies>0", id) == 1;
    }
    public static void giveBack(Connection c, int id) throws SQLException {
        update(c, "UPDATE books SET available_copies=available_copies+1 WHERE id=?", id);
    }
}
