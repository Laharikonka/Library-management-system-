package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Category;
import java.sql.SQLException;
import java.util.List;

public final class CategoryDAO {
    private CategoryDAO() {}
    public static int add(String name) throws SQLException { return insert("INSERT INTO categories(name) VALUES(?)", name); }
    public static List<Category> listAll() throws SQLException {
        return query("SELECT * FROM categories ORDER BY name", rs -> new Category(rs.getInt("id"), rs.getString("name")));
    }
}
