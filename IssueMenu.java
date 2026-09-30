package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.dao.IssueDAO;
import com.library.service.*;
import java.math.BigDecimal;

public final class IssueMenu {
    private IssueMenu() {}
    public static void run() {
        while (true) {
            System.out.println("\n-- Issue / Return --\n1. Issue a book\n2. Return a book\n3. Open loans\n0. Back");
            int c = integer("Choose: ");
            if (c == 0) return;
            attempt(() -> {
                switch (c) {
                    case 1 -> {
                        int id = IssueService.issue(integer("Student id: "), integer("Book id: "));
                        System.out.println("Issued. Loan #" + id);
                    }
                    case 2 -> {
                        BigDecimal fine = ReturnService.returnBook(integer("Loan id: "));
                        System.out.println(fine.signum() > 0 ? "Returned late. Fine: Rs " + fine : "Returned on time.");
                    }
                    case 3 -> IssueDAO.view(0, false).forEach(System.out::println);
                    default -> System.out.println("Invalid choice.");
                }
            });
        }
    }
}
