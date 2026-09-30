package com.library.dao;

import static com.library.config.DatabaseConnection.*;
import com.library.model.Notification;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public final class NotificationDAO {
    private NotificationDAO() {}
    public static void add(Connection c, int userId, String message) throws SQLException {
        insert(c, "INSERT INTO notifications(user_id,message) VALUES(?,?)", userId, message);
    }
    public static List<Notification> unread(int userId) throws SQLException {
        return query("SELECT * FROM notifications WHERE user_id=? AND is_read=0 ORDER BY id DESC",
            rs -> new Notification(rs.getInt("id"), rs.getInt("user_id"), rs.getString("message"),
                rs.getObject("created_at", LocalDateTime.class), rs.getBoolean("is_read")), userId);
    }
    public static void markAllRead(int userId) throws SQLException {
        update("UPDATE notifications SET is_read=1 WHERE user_id=?", userId);
    }
}
