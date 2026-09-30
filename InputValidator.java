package com.library.util;

import java.io.Console;
import java.util.Scanner;

/** Console input helpers and format validators. */
public final class InputValidator {
    private static final Scanner IN = new Scanner(System.in);
    private InputValidator() {}

    public interface Action { void run() throws Exception; }

    public static boolean isValidEmail(String s) { return s != null && s.matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+"); }
    public static boolean isValidUpi(String s) { return s != null && s.matches("[\\w.-]{2,}@[a-zA-Z]{2,}"); }
    public static boolean isValidCard(String s) { return s != null && s.replace(" ", "").matches("\\d{16}"); }

    public static String text(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!IN.hasNextLine()) throw new IllegalStateException("Input closed");
            String s = IN.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("This field is required.");
        }
    }
    public static int integer(String prompt) {
        while (true) {
            try { return Integer.parseInt(text(prompt)); }
            catch (NumberFormatException e) { System.out.println("Enter a whole number."); }
        }
    }
    public static String email(String prompt) {
        while (true) {
            String s = text(prompt);
            if (isValidEmail(s)) return s;
            System.out.println("Enter a valid email address.");
        }
    }
    public static String password(String prompt) {
        Console c = System.console();
        if (c == null) return text(prompt);
        char[] p = c.readPassword(prompt);
        return p == null ? "" : new String(p);
    }
    /** Runs a menu action and prints errors instead of crashing. */
    public static void attempt(Action a) {
        try { a.run(); }
        catch (IllegalArgumentException | IllegalStateException e) { System.out.println("! " + e.getMessage()); }
        catch (java.sql.SQLIntegrityConstraintViolationException e) { System.out.println("! Duplicate or linked record: " + e.getMessage()); }
        catch (Exception e) { System.out.println("! Error: " + e.getMessage()); }
    }
}
