package com.library.service;

import static com.library.config.DatabaseConnection.tx;
import com.library.dao.*;
import com.library.model.Issue;
import com.library.util.DateUtil;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ReturnService {
    private ReturnService() {}
    /** Returns a book; creates a fine when late. Gives back the fine amount (0 if on time). */
    public static BigDecimal returnBook(int issueId) throws SQLException {
        return tx(c -> {
            Issue i = IssueDAO.findById(c, issueId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
            if (i.returnDate() != null) throw new IllegalStateException("Already returned");
            LocalDate today = DateUtil.today();
            BigDecimal fine = FineService.calculate(i.dueDate(), today);
            IssueDAO.markReturned(c, issueId, today);
            ReturnDAO.insert(c, issueId, today, fine);
            BookDAO.giveBack(c, i.bookId());
            if (fine.signum() > 0) {
                FineDAO.create(c, issueId, i.studentId(), fine);
                NotificationDAO.add(c, i.studentId(), "Late return fine of Rs " + fine + " for loan #" + issueId);
            }
            return fine;
        });
    }
}
