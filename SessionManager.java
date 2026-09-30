package com.library.security;

import com.library.model.User;

public final class SessionManager {
    private static User current;
    private SessionManager() {}
    public static void login(User u) { current = u; }
    public static void logout() { current = null; }
    public static User current() { return current; }
    public static boolean isLibrarian() { return current != null && "LIBRARIAN".equals(current.getRole()); }
}
