package com.library;

import static org.junit.jupiter.api.Assertions.*;
import com.library.model.Payment;
import com.library.service.PaymentService;
import com.library.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PaymentTest {
    @Test void acceptsKnownMethodsOnly() {
        assertTrue(PaymentService.isValidMethod("UPI"));
        assertFalse(PaymentService.isValidMethod("BITCOIN"));
    }
    @Test void validatesCardAndUpi() {
        assertTrue(InputValidator.isValidCard("4111 1111 1111 1111"));
        assertFalse(InputValidator.isValidCard("1234"));
        assertTrue(InputValidator.isValidUpi("asha@okbank"));
        assertFalse(InputValidator.isValidUpi("asha"));
    }
    @Test void receiptShowsReferenceAndAmount() {
        String r = ReceiptGenerator.render(new Payment(1, 2, BigDecimal.TEN, "CASH", "C-ABC123", LocalDateTime.now()), "Asha");
        assertTrue(r.contains("C-ABC123") && r.contains("Rs 10") && r.contains("Asha"));
    }
}
