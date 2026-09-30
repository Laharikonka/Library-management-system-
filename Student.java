package com.library.model;

public class Student extends User {
    private final String rollNo, department;
    public Student(User u, String rollNo, String department) {
        super(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getPasswordHash());
        this.rollNo = rollNo; this.department = department;
    }
    public String getRollNo() { return rollNo; }
    public String getDepartment() { return department; }
}
