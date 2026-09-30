package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.model.*;
import com.library.service.*;
import com.library.util.ReceiptGenerator;
import java.util.List;

public final class PaymentMenu {
    private PaymentMenu() {}
    public static void run() {
        while (true) {
            System.out.println("\n-- Fines and Payments --\n1. Collect a student's fine\n2. All payments\n0. Back");
            int c = integer("Choose: ");
            if (c == 0) return;
            attempt(() -> {
                switch (c) {
                    case 1 -> payFines(integer("Student id: "));
                    case 2 -> print(PaymentService.all());
                    default -> System.out.println("Invalid choice.");
                }
            });
        }
    }
    public static void payFines(int studentId) throws Exception {
        List<Fine> fines = FineService.unpaid(studentId);
        if (fines.isEmpty()) { System.out.println("No pending fines."); return; }
        fines.forEach(f -> System.out.printf("Fine #%d | loan #%d | Rs %s%n", f.id(), f.issueId(), f.amount()));
        int fineId = integer("Fine id to pay (0 = cancel): ");
        if (fineId == 0) return;
        System.out.println("Method: 1. Cash  2. Card  3. UPI");
        String method = switch (integer("Choose: ")) {
            case 1 -> "CASH";
            case 2 -> "CARD";
            case 3 -> "UPI";
            default -> throw new IllegalArgumentException("Invalid method");
        };
        if (method.equals("CARD") && !isValidCard(text("Card number (16 digits, not stored): ")))
            throw new IllegalArgumentException("Invalid card number");
        if (method.equals("UPI") && !isValidUpi(text("UPI id (name@bank): ")))
            throw new IllegalArgumentException("Invalid UPI id");
        Payment p = PaymentService.pay(fineId, studentId, method);
        String name = StudentService.get(studentId).getName();
        System.out.println(ReceiptGenerator.render(p, name));
        System.out.println("Receipt saved to " + ReceiptGenerator.save(p, name));
    }
    static void print(List<Payment> list) {
        if (list.isEmpty()) System.out.println("No payments yet.");
        list.forEach(p -> System.out.printf("%s | fine #%d | Rs %s | %s | %s%n", p.txnRef(), p.fineId(), p.amount(), p.method(), p.paidAt()));
    }
}
