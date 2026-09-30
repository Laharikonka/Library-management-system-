package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.model.User;
import com.library.service.LoginService;

public final class LoginMenu {
    private LoginMenu() {}
    public static void run() {
        while (true) {
            System.out.println("\n=== Library Management System ===\n1. Login\n2. Student sign-up\n0. Exit");
            int c = integer("Choose: ");
            if (c == 0) { System.out.println("Goodbye."); return; }
            attempt(() -> {
                switch (c) {
                    case 1 -> login();
                    case 2 -> signUp();
                    default -> System.out.println("Invalid choice.");
                }
            });
        }
    }
    private static void login() throws Exception {
        User u = LoginService.login(email("Email: "), password("Password: "));
        System.out.println("Welcome, " + u.getName() + "!");
        if ("LIBRARIAN".equals(u.getRole())) LibrarianMenu.run(); else StudentMenu.run();
    }
    private static void signUp() throws Exception {
        LoginService.registerStudent(text("Name: "), email("Email: "), password("Password (min 6): "), text("Roll no: "), text("Department: "));
        System.out.println("Account created. You can log in now.");
    }
}
