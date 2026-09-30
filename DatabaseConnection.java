package com.library.config;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

/** Connection factory plus small JDBC helpers used by every DAO. */
public final class DatabaseConnection {
    private static final Properties P = new Properties();
    static {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/application.properties")) {
            P.load(in);
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    private DatabaseConnection() {}

    public interface Mapper<T> { T map(ResultSet rs) throws SQLException; }
    public interface Tx<T> { T run(Connection c) throws SQLException; }

    public static int intProp(String key) { return Integer.parseInt(P.getProperty(key)); }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(P.getProperty("db.url"), P.getProperty("db.user"), P.getProperty("db.password"));
    }

    private static void bind(PreparedStatement ps, Object[] p) throws SQLException {
        for (int i = 0; i < p.length; i++) ps.setObject(i + 1, p[i]);
    }

    /** INSERT: returns the generated key. */
    public static int insert(Connection c, String sql, Object... p) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, p); ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys();
            return k.next() ? k.getInt(1) : 0;
        }
    }
    public static int insert(String sql, Object... p) throws SQLException {
        try (Connection c = get()) { return insert(c, sql, p); }
    }

    /** UPDATE/DELETE: returns rows affected. */
    public static int update(Connection c, String sql, Object... p) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) { bind(ps, p); return ps.executeUpdate(); }
    }
    public static int update(String sql, Object... p) throws SQLException {
        try (Connection c = get()) { return update(c, sql, p); }
    }

    public static <T> List<T> query(Connection c, String sql, Mapper<T> m, Object... p) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, p);
            ResultSet rs = ps.executeQuery();
            List<T> out = new ArrayList<>();
            while (rs.next()) out.add(m.map(rs));
            return out;
        }
    }
    public static <T> List<T> query(String sql, Mapper<T> m, Object... p) throws SQLException {
        try (Connection c = get()) { return query(c, sql, m, p); }
    }

    /** Runs work in one transaction: commit on success, rollback on any failure. */
    public static <T> T tx(Tx<T> work) throws SQLException {
        try (Connection c = get()) {
            c.setAutoCommit(false);
            try {
                T r = work.run(c);
                c.commit();
                return r;
            } catch (Exception | Error e) { c.rollback(); throw e; }
        }
    }
}
