package com.library.ui;

import static com.library.util.InputValidator.attempt;
import com.library.service.ReportService;

public final class ReportMenu {
    private ReportMenu() {}
    public static void run() {
        attempt(() -> {
            System.out.println("\n-- Summary --");
            ReportService.summary().forEach(System.out::println);
            System.out.println("\n-- Overdue loans --");
            var late = ReportService.overdue();
            if (late.isEmpty()) System.out.println("None.");
            late.forEach(System.out::println);
        });
    }
}
