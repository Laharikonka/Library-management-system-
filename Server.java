package library;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/** Tiny HTTP server (JDK built-in) exposing a JSON API over JDBC + serving the HTML page. */
public class Server {
    static final int FINE_PER_DAY = 5;

    public static void main(String[] args) throws Exception {
        HttpServer s = HttpServer.create(new InetSocketAddress(8080), 0);
        s.createContext("/", Server::handle);
        s.start();
        System.out.println("Library running at http://localhost:8080");
    }

    static void handle(HttpExchange x) throws IOException {
        String path = x.getRequestURI().getPath();
        try {
            if (!path.startsWith("/api/")) { page(x); return; }
            Map<String, String> f = form(x);
            String out;
            switch (x.getRequestMethod() + " " + path) {
                case "GET /api/stats" -> out = Db.query(
                    "SELECT (SELECT COUNT(*) FROM books) books, (SELECT COUNT(*) FROM members) members,"
                    + " (SELECT COUNT(*) FROM issues WHERE return_date IS NULL) issued,"
                    + " (SELECT IFNULL(SUM(amount),0) FROM payments) collected,"
                    + " (SELECT COUNT(*) FROM issues WHERE return_date IS NULL AND due_date<CURDATE()) overdue");
                case "GET /api/books" -> out = Db.query("SELECT * FROM books ORDER BY title");
                case "POST /api/books" -> {
                    int n = Integer.parseInt(f.get("copies"));
                    Db.update("INSERT INTO books(title,author,copies,available) VALUES(?,?,?,?)",
                        f.get("title"), f.get("author"), n, n);
                    out = "{\"ok\":true}";
                }
                case "GET /api/members" -> out = Db.query("SELECT * FROM members ORDER BY name");
                case "POST /api/members" -> {
                    Db.update("INSERT INTO members(name,email) VALUES(?,?)", f.get("name"), f.get("email"));
                    out = "{\"ok\":true}";
                }
                case "GET /api/issues" -> out = Db.query(
                    "SELECT i.id, b.title, m.name, i.issue_date, i.due_date, i.return_date, i.fine, i.fine_paid,"
                    + " IF(i.return_date IS NULL AND CURDATE()>i.due_date, DATEDIFF(CURDATE(),i.due_date)*" + FINE_PER_DAY + ", i.fine) fine_now"
                    + " FROM issues i JOIN books b ON b.id=i.book_id JOIN members m ON m.id=i.member_id ORDER BY i.id DESC");
                case "POST /api/issue" -> { issue(Integer.parseInt(f.get("bookId")), Integer.parseInt(f.get("memberId"))); out = "{\"ok\":true}"; }
                case "POST /api/return" -> { giveBack(Integer.parseInt(f.get("issueId"))); out = "{\"ok\":true}"; }
                case "POST /api/pay" -> out = pay(Integer.parseInt(f.get("issueId")), f.get("method"));
                case "GET /api/payments" -> out = Db.query(
                    "SELECT p.id, b.title, m.name, p.amount, p.method, p.txn_ref, p.paid_at FROM payments p"
                    + " JOIN issues i ON i.id=p.issue_id JOIN books b ON b.id=i.book_id JOIN members m ON m.id=i.member_id ORDER BY p.id DESC");
                default -> { send(x, 404, "{\"error\":\"Not found\"}"); return; }
            }
            send(x, 200, out);
        } catch (Exception e) {
            send(x, 400, "{\"error\":\"" + Db.esc(String.valueOf(e.getMessage())) + "\"}");
        }
    }

    static void issue(int bookId, int memberId) throws Exception {
        try (Connection c = Db.get()) {
            c.setAutoCommit(false);
            try (PreparedStatement u = c.prepareStatement("UPDATE books SET available=available-1 WHERE id=? AND available>0")) {
                u.setInt(1, bookId);
                if (u.executeUpdate() == 0) throw new Exception("No copies available");
            }
            Db.exec(c, "INSERT INTO issues(book_id,member_id,issue_date,due_date) VALUES(?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 14 DAY))", bookId, memberId);
            c.commit();
        }
    }

    static void giveBack(int issueId) throws Exception {
        try (Connection c = Db.get()) {
            c.setAutoCommit(false);
            int bookId;
            try (PreparedStatement q = c.prepareStatement("SELECT book_id FROM issues WHERE id=? AND return_date IS NULL")) {
                q.setInt(1, issueId);
                ResultSet r = q.executeQuery();
                if (!r.next()) throw new Exception("Already returned");
                bookId = r.getInt(1);
            }
            Db.exec(c, "UPDATE issues SET return_date=CURDATE(), fine=GREATEST(0,DATEDIFF(CURDATE(),due_date))*? WHERE id=?", FINE_PER_DAY, issueId);
            Db.exec(c, "UPDATE books SET available=available+1 WHERE id=?", bookId);
            c.commit();
        }
    }

    /** Pays the fine. Replace the txn_ref line with a real gateway call (Razorpay/Stripe) in production. */
    static String pay(int issueId, String method) throws Exception {
        if (!List.of("CASH", "CARD", "UPI").contains(method)) throw new Exception("Choose a payment method");
        try (Connection c = Db.get()) {
            c.setAutoCommit(false);
            double fine;
            try (PreparedStatement q = c.prepareStatement("SELECT fine FROM issues WHERE id=? AND return_date IS NOT NULL AND fine_paid=0 AND fine>0")) {
                q.setInt(1, issueId);
                ResultSet r = q.executeQuery();
                if (!r.next()) throw new Exception("No unpaid fine for this loan");
                fine = r.getDouble(1);
            }
            String ref = method.substring(0, 1) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Db.exec(c, "INSERT INTO payments(issue_id,amount,method,txn_ref) VALUES(?,?,?,?)", issueId, fine, method, ref);
            Db.exec(c, "UPDATE issues SET fine_paid=1 WHERE id=?", issueId);
            c.commit();
            return "{\"ok\":true,\"txn\":\"" + ref + "\",\"amount\":" + fine + "}";
        }
    }

    static Map<String, String> form(HttpExchange x) throws IOException {
        String body = new String(x.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (body.isEmpty() && x.getRequestURI().getRawQuery() != null) body = x.getRequestURI().getRawQuery();
        Map<String, String> m = new HashMap<>();
        for (String kv : body.split("&")) {
            String[] p = kv.split("=", 2);
            if (p.length == 2) m.put(URLDecoder.decode(p[0], StandardCharsets.UTF_8), URLDecoder.decode(p[1], StandardCharsets.UTF_8));
        }
        return m;
    }

    static void page(HttpExchange x) throws IOException {
        try (InputStream in = Server.class.getResourceAsStream("/public/index.html")) {
            send(x, 200, "text/html; charset=utf-8", new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    static void send(HttpExchange x, int code, String body) throws IOException { send(x, code, "application/json", body); }

    static void send(HttpExchange x, int code, String type, String body) throws IOException {
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        x.getResponseHeaders().set("Content-Type", type);
        x.sendResponseHeaders(code, b.length);
        try (OutputStream o = x.getResponseBody()) { o.write(b); }
    }
}
