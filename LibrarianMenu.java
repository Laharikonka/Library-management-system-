package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.service.*;

public final class LibrarianMenu {
    private LibrarianMenu() {}
    public static void run() {
        while (true) {
            System.out.println("\n-- Librarian --\n1. Books\n2. Issue / return\n3. Fines and payments\n4. Reports\n5. List students\n6. Add librarian\n0. Logout");
            int c = integer("Choose: ");
            if (c == 0) { LoginService.logout(); return; }
            switch (c) {
                case 1 -> BookMenu.manage();
                case 2 -> IssueMenu.run();
                case 3 -> PaymentMenu.run();
                case 4 -> ReportMenu.run();
                case 5 -> attempt(() -> StudentService.all().forEach(s ->
                    System.out.printf("#%d %s | %s | %s | %s%n", s.getId(), s.getName(), s.getEmail(), s.getRollNo(), s.getDepartment())));
                case 6 -> attempt(() -> {
                    LibrarianService.add(text("Name: "), email("Email: "), password("Password (min 6): "), text("Employee id: "));
                    System.out.println("Librarian added.");
                });
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
