package library;

import java.sql.*;

/** JDBC helper: connections, parameterised queries, JSON output. */
public class Db {
    static String env(String k, String d) { String v = System.getenv(k); return v == null ? d : v; }

    static Connection get() throws SQLException {
        return DriverManager.getConnection(
            env("DB_URL", "jdbc:mysql://localhost:3306/librarydb"),
            env("DB_USER", "root"), env("DB_PASS", "root"));
    }

    static void bind(PreparedStatement ps, Object[] p) throws SQLException {
        for (int i = 0; i < p.length; i++) ps.setObject(i + 1, p[i]);
    }

    static void exec(Connection c, String sql, Object... p) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) { bind(ps, p); ps.executeUpdate(); }
    }

    static void update(String sql, Object... p) throws SQLException {
        try (Connection c = get()) { exec(c, sql, p); }
    }

    static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ");
    }

    static String query(String sql, Object... p) throws SQLException {
        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, p);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            StringBuilder sb = new StringBuilder("[");
            while (rs.next()) {
                if (sb.length() > 1) sb.append(',');
                sb.append('{');
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    if (i > 1) sb.append(',');
                    Object o = rs.getObject(i);
                    sb.append('"').append(md.getColumnLabel(i)).append("\":");
                    if (o == null) sb.append("null");
                    else if (o instanceof Number || o instanceof Boolean) sb.append(o);
                    else sb.append('"').append(esc(o.toString())).append('"');
                }
                sb.append('}');
            }
            return sb.append(']').toString();
        }
    }
}
