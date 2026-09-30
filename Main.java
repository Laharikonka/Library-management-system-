package com.library;

import com.library.service.LoginService;
import com.library.ui.LoginMenu;

public class Main {
    public static void main(String[] args) {
        try {
            if (LoginService.ensureDefaultAdmin())
                System.out.println("First run: created librarian admin@library.com / admin123 (change it).");
        } catch (Exception e) {
            System.out.println("Cannot reach the database: " + e.getMessage());
            System.out.println("Check application.properties and that database/*.sql was run.");
            return;
        }
        LoginMenu.run();
    }
}
