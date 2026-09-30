package com.library.model;

public class Librarian extends User {
    private final String employeeId;
    public Librarian(User u, String employeeId) {
        super(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getPasswordHash());
        this.employeeId = employeeId;
    }
    public String getEmployeeId() { return employeeId; }
}
