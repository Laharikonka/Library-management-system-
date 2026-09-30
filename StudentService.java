package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import java.sql.SQLException;
import java.util.*;

public final class StudentService {
    private StudentService() {}
    public static List<Student> all() throws SQLException { return StudentDAO.listAll(); }
    public static Student get(int id) throws SQLException {
        return StudentDAO.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }
}
