package com.library.service;

import static com.library.config.DatabaseConnection.tx;
import com.library.dao.*;
import com.library.model.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public final class PaymentService {
    private PaymentService() {}
    public static boolean isValidMethod(String m) { return List.of("CASH", "CARD", "UPI").contains(m); }

    /** Pays one fine. Swap the reference below for a real gateway call in production. */
    public static Payment pay(int fineId, int studentId, String method) throws SQLException {
        if (!isValidMethod(method)) throw new IllegalArgumentException("Choose Cash, Card or UPI");
        return tx(c -> {
            Fine f = FineDAO.findById(c, fineId).orElseThrow(() -> new IllegalArgumentException("Fine not found"));
            if (f.studentId() != studentId) throw new IllegalArgumentException("That fine belongs to another student");
            if (f.paid()) throw new IllegalStateException("Fine already paid");
            String ref = method.charAt(0) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            int id = PaymentDAO.insert(c, fineId, f.amount(), method, ref);
            FineDAO.markPaid(c, fineId);
            NotificationDAO.add(c, studentId, "Payment of Rs " + f.amount() + " received (" + ref + ")");
            return new Payment(id, fineId, f.amount(), method, ref, LocalDateTime.now());
        });
    }
    public static List<Payment> history(int studentId) throws SQLException { return PaymentDAO.byStudent(studentId); }
    public static List<Payment> all() throws SQLException { return PaymentDAO.all(); }
}
