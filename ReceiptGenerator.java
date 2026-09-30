package com.library.util;

import com.library.model.Payment;
import java.io.IOException;
import java.nio.file.*;

public final class ReceiptGenerator {
    private ReceiptGenerator() {}
    public static String render(Payment p, String studentName) {
        return String.join("\n",
            "=========== LIBRARY FINE RECEIPT ===========",
            "Reference : " + p.txnRef(),
            "Student   : " + studentName,
            "Fine no.  : " + p.fineId(),
            "Amount    : Rs " + p.amount(),
            "Method    : " + p.method(),
            "Paid at   : " + p.paidAt(),
            "============================================");
    }
    public static Path save(Payment p, String studentName) throws IOException {
        Path dir = Paths.get("receipts");
        Files.createDirectories(dir);
        return Files.writeString(dir.resolve("receipt-" + p.txnRef() + ".txt"), render(p, studentName));
    }
}
