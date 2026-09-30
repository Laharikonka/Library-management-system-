package com.library.model;

public class User {
    private final int id;
    private final String name, email, role, passwordHash;
    public User(int id, String name, String email, String role, String passwordHash) {
        this.id = id; this.name = name; this.email = email; this.role = role; this.passwordHash = passwordHash;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }
}
