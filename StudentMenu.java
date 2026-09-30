package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.dao.*;
import com.library.security.SessionManager;
import com.library.service.*;

public final class StudentMenu {
    private StudentMenu() {}
    public static void run() {
        int me = SessionManager.current().getId();
        try {
            int n = NotificationDAO.unread(me).size();
            if (n > 0) System.out.println("You have " + n + " new notification(s).");
        } catch (Exception ignored) { }
        while (true) {
            System.out.println("\n-- Student --\n1. Search books\n2. My loans\n3. My fines and payment\n4. Payment history\n5. Notifications\n0. Logout");
            int c = integer("Choose: ");
            if (c == 0) { LoginService.logout(); return; }
            attempt(() -> {
                switch (c) {
                    case 1 -> BookMenu.search();
                    case 2 -> IssueDAO.view(me, false).forEach(System.out::println);
                    case 3 -> PaymentMenu.payFines(me);
                    case 4 -> PaymentMenu.print(PaymentService.history(me));
                    case 5 -> {
                        NotificationDAO.unread(me).forEach(x -> System.out.println("* " + x.message()));
                        NotificationDAO.markAllRead(me);
                    }
                    default -> System.out.println("Invalid choice.");
                }
            });
        }
    }
}
