package com.library.service;

import static com.library.config.DatabaseConnection.*;
import com.library.dao.*;
import com.library.model.Book;
import com.library.util.DateUtil;
import java.sql.SQLException;
import java.time.LocalDate;

public final class IssueService {
    private IssueService() {}
    /** Issues a book in one transaction; returns the loan id. */
    public static int issue(int studentId, int bookId) throws SQLException {
        StudentDAO.findByUserId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Book book = BookDAO.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (FineService.totalUnpaid(studentId).signum() > 0) throw new IllegalStateException("Student has unpaid fines");
        return tx(c -> {
            if (IssueDAO.countActive(c, studentId) >= intProp("max.books"))
                throw new IllegalStateException("Loan limit of " + intProp("max.books") + " books reached");
            if (!BookDAO.take(c, bookId)) throw new IllegalStateException("No copies available");
            LocalDate today = DateUtil.today(), due = today.plusDays(intProp("loan.days"));
            int id = IssueDAO.create(c, bookId, studentId, today, due);
            NotificationDAO.add(c, studentId, "Issued '" + book.title() + "', due " + DateUtil.format(due));
            return id;
        });
    }
}
